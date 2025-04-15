package com.assignment.job_scheduler.service;

import com.assignment.job_scheduler.model.Job;
import com.assignment.job_scheduler.model.JobStatus;
import com.assignment.job_scheduler.model.JobType;
import com.assignment.job_scheduler.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final KafkaProducerService kafkaProducerService;
    private final MinioService minioService;

    public Job createJob(Job job) {
        job.setId(null);
        job.setStatus(JobStatus.SCHEDULED);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(UUID id) {
        return jobRepository.findById(id);
    }

    public void updateJobStatus(UUID jobId, JobStatus status) {
        jobRepository.findById(jobId).ifPresent(job -> {
            job.setStatus(status);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
        });
    }

    @Scheduled(fixedRate = 60000)
    public void processDelayedMessages() {
        List<Job> delayedJobs = jobRepository.findByTypeAndStatus(JobType.DELAYED_MESSAGE, JobStatus.SCHEDULED);

        for (Job job : delayedJobs) {
            if (job.getScheduledTime().isBefore(LocalDateTime.now())) {
                kafkaProducerService.sendMessage(job.getKafkaTopic(), job.getKafkaMetadata());

                if (job.getBinaryPath() != null && !job.getBinaryPath().isBlank()) {
                    executeBinaryJob(job);
                } else {
                    job.setStatus(JobStatus.SUCCESS);
                    job.setUpdatedAt(LocalDateTime.now());
                    jobRepository.save(job);
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processRecurringJobs() {
        List<Job> recurringJobs = jobRepository.findByStatus(JobStatus.SCHEDULED);

        for (Job job : recurringJobs) {
            if (isRecurring(job.getType()) && job.getScheduledTime() != null
                    && job.getScheduledTime().isBefore(LocalDateTime.now())) {

                if (job.getBinaryPath() != null && !job.getBinaryPath().isBlank()) {
                    executeBinaryJob(job);
                }

                job.setScheduledTime(getNextRunTime(job.getScheduledTime(), job.getType()));
                job.setStatus(JobStatus.SCHEDULED);
                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);
            }
        }
    }

    public boolean deleteJob(UUID id) {
    if (jobRepository.existsById(id)) {
        jobRepository.deleteById(id);
        return true;
    }
    return false;
    }


    // ✅ Unified execution dispatcher
    private void executeBinaryJob(Job job) {
        String path = job.getBinaryPath();
        if (path == null || path.isBlank()) {
            job.setStatus(JobStatus.FAILED);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
            return;
        }

        if (path.endsWith(".jar")) {
            executeJarJob(job);
        } else if (path.endsWith(".zip")) {
            executeNpmJob(job);
        } else {
            System.out.println("Unsupported file type: " + path);
            job.setStatus(JobStatus.FAILED);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepository.save(job);
        }
    }

    // ✅ Execute a JAR file
    private void executeJarJob(Job job) {
        try {
            String objectName = job.getBinaryPath().substring(job.getBinaryPath().indexOf("/") + 1);
            File file = minioService.downloadFile(objectName);

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", file.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            printProcessOutput(process);
            int exitCode = process.waitFor();
            job.setStatus(exitCode == 0 ? JobStatus.SUCCESS : JobStatus.FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            job.setStatus(JobStatus.FAILED);
        }

        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    // ✅ Execute a Node.js project from a ZIP file
    private void executeNpmJob(Job job) {
        try {
            String objectName = job.getBinaryPath().substring(job.getBinaryPath().indexOf("/") + 1);
            File zipFile = minioService.downloadFile(objectName);

            File unzipDir = new File(System.getProperty("java.io.tmpdir"), "job_" + System.currentTimeMillis());
            unzipDir.mkdirs();

            try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
                ZipEntry entry;
                while ((entry = zipIn.getNextEntry()) != null) {
                    File newFile = new File(unzipDir, entry.getName());
                    if (entry.isDirectory()) {
                        newFile.mkdirs();
                    } else {
                        newFile.getParentFile().mkdirs();
                        try (FileOutputStream out = new FileOutputStream(newFile)) {
                            zipIn.transferTo(out);
                        }
                    }
                    zipIn.closeEntry();
                }
            }

            ProcessBuilder install = new ProcessBuilder("C:\\Program Files\\nodejs\\npm.cmd", "install");
            install.directory(unzipDir);
            install.redirectErrorStream(true);
            Process p1 = install.start();
            printProcessOutput(p1);
            int installExit = p1.waitFor();
            if (installExit != 0) throw new RuntimeException("npm install failed");

            ProcessBuilder start = new ProcessBuilder("C:\\Program Files\\nodejs\\npm.cmd", "run", "start");

            start.directory(unzipDir);
            start.redirectErrorStream(true);
            Process p2 = start.start();
            printProcessOutput(p2);
            int startExit = p2.waitFor();

            job.setStatus(startExit == 0 ? JobStatus.SUCCESS : JobStatus.FAILED);

        } catch (Exception e) {
            e.printStackTrace();
            job.setStatus(JobStatus.FAILED);
        }

        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    // ✅ Output helper
    private void printProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SCRIPT OUTPUT] " + line);
            }
        }
    }

    private boolean isRecurring(JobType type) {
        return type == JobType.HOURLY || type == JobType.DAILY ||
               type == JobType.WEEKLY || type == JobType.MONTHLY;
    }

    private LocalDateTime getNextRunTime(LocalDateTime current, JobType type) {
        return switch (type) {
            case HOURLY -> current.plusHours(1);
            case DAILY -> current.plusDays(1);
            case WEEKLY -> current.plusWeeks(1);
            case MONTHLY -> current.plusMonths(1);
            default -> current;
        };
    }
}
