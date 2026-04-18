package com.example.ecommerce.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;

public class OrderExportJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(OrderExportJob.class);

    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job orderExportJob;

    public OrderExportJob(JobLauncher jobLauncher, @Qualifier("orderExportJob") org.springframework.batch.core.Job orderExportJob) {
        this.jobLauncher = jobLauncher;
        this.orderExportJob = orderExportJob;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Starting daily order export job...");

        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(orderExportJob, params);
            logger.info("Daily order export job completed successfully.");
        } catch (Exception e) {
            logger.error("Daily order export job failed: {}", e.getMessage(), e);
            throw new JobExecutionException(e);
        }
    }
}
