package com.notification.service.job;

import com.notification.service.kafka.dto.Place;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class NotificationJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var data = context.getJobDetail().getJobDataMap();

        var user = data.getString("user");
        var film = data.getString("film");
        var startTime = (Instant) data.get("startTime");
        var place = (Place) data.get("place");

       log.info("TO: '{}' send reminder about film: '{}' which starts at: '{}'. User place: '{}'",
               user, film, startTime, place);
    }
}
