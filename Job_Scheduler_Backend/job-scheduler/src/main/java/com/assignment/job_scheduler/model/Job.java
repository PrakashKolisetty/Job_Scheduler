package com.assignment.job_scheduler.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a job to be scheduled and executed")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique Job ID", example = "b73dfc15-9d10-4b45-b88d-92e6bba4e014")
    private UUID id;

    @Schema(description = "Name of the job", example = "Sync Job")
    private String name;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of the job", example = "ONE_TIME", allowableValues = {"IMMEDIATE", "ONE_TIME", "RECURRING", "DELAYED_MESSAGE", "HOURLY", "DAILY", "WEEKLY", "MONTHLY"})
    private JobType type;

    @Schema(description = "Cron expression for recurring jobs (optional)", example = "0 0 * * *")
    private String cronExpression;

    @Schema(description = "Time when the job is scheduled to run", example = "2025-04-14T15:30:00")
    private LocalDateTime scheduledTime;

    @Schema(description = "Time zone for scheduling the job", example = "Asia/Kolkata")
    private String timeZone;

    @Schema(description = "Path to the binary in MinIO", example = "job-binaries/my-job.jar")
    private String binaryPath;

    @Schema(description = "Kafka topic where message should be sent", example = "sync-topic")
    private String kafkaTopic;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Kafka metadata in JSON format", example = "{\"key\":\"value\"}")
    private String kafkaMetadata;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the job", example = "SCHEDULED", allowableValues = {"SCHEDULED", "RUNNING", "SUCCESS", "FAILED"})
    private JobStatus status;

    @Schema(description = "Time when the job was created", example = "2025-04-13T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Time when the job was last updated", example = "2025-04-13T14:05:00")
    private LocalDateTime updatedAt;
}
