package com.binaracademy.shoppingservice.job;

import com.binaracademy.shoppingservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@RequiredArgsConstructor
public class OrderCleanupJob implements Job {
    private final OrderService orderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        orderService.deleteOrdersOlderThanThreeMonths();
    }
}
