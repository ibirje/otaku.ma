package com.otakuma.migration;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableBatchProcessing
public class MigrationApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MigrationApplication.class);

    @Autowired
    @Qualifier("syncJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("migrationJob")
    private Job migrationJob;

    @Autowired
    @Qualifier("verificationJob")
    private Job verificationJob;

    public static void main(String[] args) {
        SpringApplication.run(MigrationApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Run migration job first and wait for it to complete
            logger.info("Starting migration job...");
            JobParameters migrationParams = new JobParametersBuilder()
                    .addDate("run.id", new Date())
                    .addString("job.name", "migrationJob")
                    .toJobParameters();
            
            JobExecution migrationExecution = jobLauncher.run(migrationJob, migrationParams);
            logger.info("Migration job complete with status: {}", migrationExecution.getStatus());
            
            // Only proceed with verification if migration was successful
            if (migrationExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                logger.info("Starting verification job...");
                JobParameters verificationParams = new JobParametersBuilder()
                        .addDate("run.id", new Date())
                        .addString("job.name", "verificationJob")
                        .toJobParameters();
                
                JobExecution verificationExecution = jobLauncher.run(verificationJob, verificationParams);
                logger.info("Verification job complete with status: {}", verificationExecution.getStatus());
            } else {
                logger.error("Migration job failed with status {}, skipping verification", 
                        migrationExecution.getExitStatus());
            }
            
            // Exit the application after both jobs complete
            logger.info("All jobs completed. Exiting application.");
            System.exit(0);
        } catch (Exception e) {
            logger.error("Error running jobs", e);
            System.exit(1);
        }
    }
}