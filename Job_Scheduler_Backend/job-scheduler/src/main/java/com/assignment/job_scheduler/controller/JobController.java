package com.assignment.job_scheduler.controller;

import com.assignment.job_scheduler.model.Job;
import com.assignment.job_scheduler.model.JobStatus;
import com.assignment.job_scheduler.service.JobService;
import com.assignment.job_scheduler.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final MinioService minioService;

    // ➕ Create a job
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobService.createJob(job);
        return ResponseEntity.ok(savedJob);
    }

    // 📋 Get all jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // 🔍 Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        Optional<Job> job = jobService.getJobById(id);
        return job.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 📦 Upload binary file to MinIO
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBinary(@RequestParam("file") MultipartFile file) {
        String filePath = minioService.uploadFile(file);
        return ResponseEntity.ok(filePath);
    }

    // 🛠 Update job status (optional)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateJobStatus(@PathVariable UUID id, @RequestParam String status) {
        try {
            JobStatus jobStatus = JobStatus.valueOf(status.toUpperCase());
            jobService.updateJobStatus(id, jobStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        boolean deleted = jobService.deleteJob(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
