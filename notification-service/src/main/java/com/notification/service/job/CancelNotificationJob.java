package com.notification.service.job;

import com.notification.service.kafka.dto.Place;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class CancelNotificationJob implements Job {

    private final NotificationAck notificationAck;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var data = context.getJobDetail().getJobDataMap();

        var user = data.getString("user");
        var film = data.getString("film");
        var startTime = (Instant) data.get("startTime");

        log.info("TO: '{}' send cancel information about film: '{}' which should start at: '{}'.",
                user, film, startTime);
    }
}
