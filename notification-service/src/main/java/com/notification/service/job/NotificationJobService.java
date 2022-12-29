package com.notification.service.job;

import com.notification.service.kafka.dto.CancelNotificationRequest;
import com.notification.service.kafka.dto.NotificationRequest;
import com.notification.service.kafka.dto.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationJobService {

    private final Scheduler scheduler;
    private final NotificationAck notificationAck;

    private final static String CANCEL_GROUP = "cancel";
    private final static String NOTIFICATION_GROUP = "notification";

    private final static int BUFFOR = 15;

    public void createNotificationJob(NotificationRequest request) {
        log.info("Sending notification for request: {}", request);
        JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                .usingJobData(prepareJobDataMap(request))
                .withIdentity(createJobKey(request))
                .build();


        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(request.getShowTime().minus(BUFFOR, ChronoUnit.MINUTES)))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);

            log.info("Notification sent successful");
            notificationAck.sendNotificationAck(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            log.error("Job for film : {} and user: {} scheduling failed",
                    request.getFilmName(), request.getUser(), ex);
            notificationAck.sendNotificationAck(ResponseStatus.FAILED);
        }
    }

    public void createCancelNotificationJob(CancelNotificationRequest request) {
        log.info("Sending cancel notification for request: {}", request);

        JobDetail jobDetail = JobBuilder.newJob(CancelNotificationJob.class)
                .usingJobData(prepareCancelJobDataMap(request))
                .withIdentity(createCancelJobKey(request))
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(java.util.Date.from(Instant.now()))
                .build();

        try {
            scheduler.getTriggersOfJob(createJobKey(request))
                            .forEach(t -> {
                                try {
                                    scheduler.unscheduleJob(t.getKey());
                                } catch (SchedulerException e) {
                                    log.error("Error during unschedulling trigger with key : {}",
                                            t.getKey());
                                }
                            });

            scheduler.scheduleJob(jobDetail, trigger);

            log.info("Cancel notification sent successful");
            notificationAck.sendNotificationAck(ResponseStatus.SUCCESS);
        } catch (Exception ex) {
            log.error("Job for cancelling film: '{}' for user: '{}' failed",
                    request.getFilmName(), request.getUser());
            notificationAck.sendNotificationAck(ResponseStatus.FAILED);
        }
    }

    private JobDataMap prepareJobDataMap(NotificationRequest request) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("user", request.getUser());
        jobDataMap.put("film", request.getFilmName());
        jobDataMap.put("startTime", request.getShowTime());
        jobDataMap.put("place", request.getPlace());
        return jobDataMap;
    }

    private JobDataMap prepareCancelJobDataMap(CancelNotificationRequest request) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("user", request.getUser());
        jobDataMap.put("film", request.getFilmName());
        jobDataMap.put("startTime", request.getShowTime());
        return jobDataMap;
    }

    private JobKey createJobKey(NotificationRequest request) {
        var name = String.format("%s_%s_%d", request.getUser(), request.getFilmName(), request.getShowTime().toEpochMilli());
        return new JobKey(name, NOTIFICATION_GROUP);
    }

    private JobKey createJobKey(CancelNotificationRequest request) {
        var name = String.format("%s_%s_%d", request.getUser(), request.getFilmName(), request.getShowTime().toEpochMilli());
        return new JobKey(name, NOTIFICATION_GROUP);
    }

    private JobKey createCancelJobKey(CancelNotificationRequest request) {
        var name = String.format("%s_%s_%d", request.getUser(), request.getFilmName(), request.getShowTime().toEpochMilli());
        return new JobKey(name, CANCEL_GROUP);
    }
}
