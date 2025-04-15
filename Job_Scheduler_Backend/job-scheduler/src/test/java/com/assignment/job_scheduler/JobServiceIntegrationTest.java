// package com.assignment.job_scheduler;

// import com.assignment.job_scheduler.model.Job;
// import com.assignment.job_scheduler.model.JobStatus;
// import com.assignment.job_scheduler.model.JobType;
// import com.assignment.job_scheduler.repository.JobRepository;
// import com.assignment.job_scheduler.service.JobService;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import java.time.LocalDateTime;
// import java.util.UUID;

// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
// @ExtendWith(SpringExtension.class)
// @Testcontainers
// public class JobServiceIntegrationTest {

//     @Container
//     static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.1")
//             .withDatabaseName("testdb")
//             .withUsername("testuser")
//             .withPassword("testpass");

//     @DynamicPropertySource
//     static void registerDbProperties(DynamicPropertyRegistry registry) {
//         registry.add("spring.datasource.url", postgres::getJdbcUrl);
//         registry.add("spring.datasource.username", postgres::getUsername);
//         registry.add("spring.datasource.password", postgres::getPassword);
//     }

//     @Autowired
//     private JobService jobService;

//     @Autowired
//     private JobRepository jobRepository;

//     @Test
//     void testCreateJob_persistsInDatabase() {
//         Job job = Job.builder()
//                 .name("Test Job")
//                 .type(JobType.ONE_TIME)
//                 .scheduledTime(LocalDateTime.now().plusMinutes(1))
//                 .timeZone("Asia/Kolkata")
//                 .status(JobStatus.SCHEDULED)
//                 .kafkaTopic("test-topic")
//                 .kafkaMetadata("{\"run\": \"now\"}")
//                 .build();

//         Job saved = jobService.createJob(job);

//         assertThat(saved.getId()).isNotNull();
//         assertThat(jobRepository.findById(saved.getId())).isPresent();
//     }
// }
