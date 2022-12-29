package com.notification.service.job;

import com.notification.service.kafka.dto.NotificationRequest;
import com.notification.service.kafka.dto.ResponseStatus;
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
    private final NotificationAck notificationAck;

    private final static int BUFFOR = 15;

    public void createNotificationJob(NotificationRequest request) {
        log.info("Sending notification for request: {}", request);
        JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                .usingJobData(prepareJobDataMap(request))
                .build();


        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(request.getShowTime().minus(BUFFOR, ChronoUnit.MINUTES)))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception ex) {
            log.error("Job for film : {} and user: {} scheduling failed",
                    request.getFilmName(), request.getUser(), ex);
            notificationAck.sendNotificationAck(ResponseStatus.FAILED);
        }
        log.info("Notification sent successful");
        notificationAck.sendNotificationAck(ResponseStatus.SUCCESS);

    }

    private JobDataMap prepareJobDataMap(NotificationRequest request) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("user", request.getUser());
        jobDataMap.put("film", request.getFilmName());
        jobDataMap.put("startTime", request.getShowTime());
        jobDataMap.put("place", request.getPlace());
        return jobDataMap;
    }
}
