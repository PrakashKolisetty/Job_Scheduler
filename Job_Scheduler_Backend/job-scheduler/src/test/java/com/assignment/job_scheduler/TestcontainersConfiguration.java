// package com.assignment.job_scheduler;

// import org.springframework.boot.test.context.TestConfiguration;
// import org.testcontainers.containers.KafkaContainer;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.DependsOn;
// import org.springframework.jdbc.datasource.DriverManagerDataSource;

// import javax.sql.DataSource;

// @TestConfiguration
// @Testcontainers
// public class TestcontainersConfiguration {

//     @Container
//     public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
//             .withDatabaseName("testdb")
//             .withUsername("testuser")
//             .withPassword("testpass");

//     @Container
//     public static KafkaContainer kafkaContainer = new KafkaContainer("7.4.0");

//     static {
//         postgresContainer.start();
//         kafkaContainer.start();
//     }

//     @Bean
//     @DependsOn("postgresContainer")
//     public DataSource dataSource() {
//         DriverManagerDataSource dataSource = new DriverManagerDataSource();
//         dataSource.setDriverClassName(postgresContainer.getDriverClassName());
//         dataSource.setUrl(postgresContainer.getJdbcUrl());
//         dataSource.setUsername(postgresContainer.getUsername());
//         dataSource.setPassword(postgresContainer.getPassword());
//         return dataSource;
//     }

//     @Bean
//     public String kafkaBootstrapServers() {
//         return kafkaContainer.getBootstrapServers();
//     }
// }
