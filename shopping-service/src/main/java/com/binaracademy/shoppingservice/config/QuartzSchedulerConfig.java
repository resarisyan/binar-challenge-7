package com.binaracademy.shoppingservice.config;

import com.binaracademy.shoppingservice.job.OrderCleanupJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzSchedulerConfig {
    @Bean
    public JobDetail orderCleanupJobDetail() {
        return JobBuilder.newJob(OrderCleanupJob.class)
                .withIdentity("orderCleanupJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger orderCleanupTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(24)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(orderCleanupJobDetail())
                .withIdentity("orderCleanupTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}

