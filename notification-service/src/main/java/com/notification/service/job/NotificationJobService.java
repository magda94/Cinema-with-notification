package com.notification.service.job;

import com.notification.service.kafka.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationJobService {

    private final Scheduler scheduler;

    private final static int BUFFOR = 15;

    public void createNotificationJob(NotificationRequest request) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class).build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(request.getShowTime().minus(BUFFOR, ChronoUnit.MINUTES)))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
