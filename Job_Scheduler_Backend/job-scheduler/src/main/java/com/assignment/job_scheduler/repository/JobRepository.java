package com.assignment.job_scheduler.repository;

import com.assignment.job_scheduler.model.Job;
import com.assignment.job_scheduler.model.JobStatus;
import com.assignment.job_scheduler.model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByTypeAndStatus(JobType type, JobStatus status);
    List<Job> findByStatus(com.assignment.job_scheduler.model.JobStatus status);

}
