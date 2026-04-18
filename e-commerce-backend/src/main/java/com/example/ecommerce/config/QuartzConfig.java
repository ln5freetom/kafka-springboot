package com.example.ecommerce.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.ecommerce.scheduler.OrderExportJob;

@Configuration
public class QuartzConfig {

    @Value("${app.export.cron:0 0 1 * * ?}")
    private String cronExpression;

    @Bean
    public JobDetailFactoryBean orderExportJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(OrderExportJob.class);
        factory.setName("orderExportJob");
        factory.setDescription("Daily export of all customer orders");
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean orderExportCronTrigger(JobDetail orderExportJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(orderExportJobDetail);
        factory.setName("orderExportCronTrigger");
        factory.setCronExpression(cronExpression);
        return factory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger... triggers) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setTriggers(triggers);
        return factory;
    }
}
