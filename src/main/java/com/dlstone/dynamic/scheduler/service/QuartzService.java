package com.dlstone.dynamic.scheduler.service;

import com.dlstone.dynamic.scheduler.configuration.JobTemplate;
import com.dlstone.dynamic.scheduler.model.SchedulerTask;
import com.dlstone.dynamic.scheduler.model.SchedulerTaskFactory;
import com.dlstone.dynamic.scheduler.wrapper.SchedulerWrapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuartzService {

    private SchedulerWrapper schedulerWrapper;

    @Autowired
    public QuartzService(SchedulerWrapper schedulerWrapper) {
        this.schedulerWrapper = schedulerWrapper;
    }

    public List<SchedulerTask> getAllSchedulerTasks() {
        return schedulerWrapper.getJobGroupNames()
            .stream()
            .flatMap(groupName -> schedulerWrapper.getJobKeys(GroupMatcher.jobGroupEquals(groupName)).stream())
            .flatMap(jobKey -> getSchedulerTasksByJobKey(schedulerWrapper, jobKey).stream())
            .collect(Collectors.toList());
    }

    private List<SchedulerTask> getSchedulerTasksByJobKey(SchedulerWrapper schedulerWrapper, JobKey jobKey) {
        JobDetail jobDetail = schedulerWrapper.getJobDetail(jobKey);
        return schedulerWrapper.getTriggersOfJob(jobKey)
            .stream()
            .map(trigger -> SchedulerTaskFactory.generateSchedulerTask(schedulerWrapper, jobDetail, trigger))
            .collect(Collectors.toList());
    }

    @Transactional
    public void addJob(SchedulerTask schedulerTask) {
        JobDetail jobDetail = newJobDetail(schedulerTask, schedulerWrapper);
        CronTrigger cronTrigger = newCronTrigger(schedulerTask, jobDetail);
        schedulerWrapper.scheduleJob(jobDetail, cronTrigger);
    }

    @Transactional
    public void deleteJob(String jobGroup, String jobName) {
        schedulerWrapper.deleteJob(new JobKey(jobGroup, jobName));
    }

    private CronTrigger newCronTrigger(SchedulerTask schedulerTask, JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withDescription(LocalDateTime.now().toString())
            .withIdentity(schedulerTask.getJobTriggerName(), schedulerTask.getJobTriggerGroupName())
            .withSchedule(CronScheduleBuilder
                .cronSchedule(schedulerTask.getJobTriggerCronExpression())
                .withMisfireHandlingInstructionDoNothing())
            .build();
    }

    private JobDetail newJobDetail(SchedulerTask schedulerTask, SchedulerWrapper schedulerWrapper) {
        JobKey jobKey = newJobKey(schedulerTask, schedulerWrapper);

        return JobBuilder.newJob()
            .ofType(JobTemplate.class)
            .storeDurably()
            .withIdentity(jobKey)
            .withDescription(schedulerTask.getJobDescription())
            .usingJobData(schedulerTask.getJobDataMap())
            .build();
    }

    private JobKey newJobKey(SchedulerTask schedulerTask, SchedulerWrapper schedulerWrapper) {
        JobKey jobKey = new JobKey(schedulerTask.getJobName(), schedulerTask.getJobGroup());
        if (schedulerWrapper.checkExists(jobKey)) {
            log.error("jobKey exist, {}", jobKey);
            throw new RuntimeException(String.format("job exist, jobName: %s, jobGroupName: %s", jobKey.getName(), jobKey.getGroup()));
        }
        return jobKey;
    }
}
