package com.dlstone.dynamic.scheduler.service;

import com.dlstone.dynamic.scheduler.mapper.JobHistoryMapper;
import com.dlstone.dynamic.scheduler.model.JobHistory;
import com.dlstone.dynamic.scheduler.quartz.JobTemplate;
import com.dlstone.dynamic.scheduler.model.SchedulerTask;
import com.dlstone.dynamic.scheduler.model.SchedulerTaskFactory;
import com.dlstone.dynamic.scheduler.quartz.wrapper.SchedulerWrapper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuartzService {

    private SchedulerWrapper schedulerWrapper;
    private JobHistoryMapper jobHistoryMapper;

    @Autowired
    public QuartzService(SchedulerWrapper schedulerWrapper, JobHistoryMapper jobHistoryMapper) {
        this.schedulerWrapper = schedulerWrapper;
        this.jobHistoryMapper = jobHistoryMapper;
    }

    public List<SchedulerTask> getAllSchedulerTasks() {
        List<SchedulerTask> schedulerTasks = schedulerWrapper.getJobGroupNames()
            .stream()
            .flatMap(groupName -> schedulerWrapper.getJobKeys(GroupMatcher.jobGroupEquals(groupName)).stream())
            .flatMap(jobKey -> getSchedulerTasksByJobKey(schedulerWrapper, jobKey).stream())
            .collect(Collectors.toList());

        return assembleLatestJobHistory(schedulerTasks);
    }

    private List<SchedulerTask> assembleLatestJobHistory(List<SchedulerTask> schedulerTasks) {
        Map<String, JobHistory> jobHistoryMap = jobHistoryMapper.findAllLatestJobHistories()
            .stream()
            .collect(Collectors.toMap(jobHistory -> jobHistory.getJobName() + jobHistory.getJobGroup(), Function.identity()));

        return schedulerTasks
            .stream()
            .peek(schedulerTask -> jobHistoryMap.getOrDefault(schedulerTask.getJobName() + schedulerTask.getJobGroup(), null))
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
        JobKey jobKey = newJobKey(schedulerTask, schedulerWrapper);
        JobDetail jobDetail = newJobDetail(schedulerTask, jobKey);
        CronTrigger cronTrigger = newCronTrigger(schedulerTask, jobDetail);
        schedulerWrapper.scheduleJob(jobDetail, cronTrigger);
    }

    @Transactional
    public void deleteJob(String jobName, String jobGroup) {
        JobKey jobKey = validateJobKey(jobName, jobGroup);
        schedulerWrapper.deleteJob(jobKey);
    }

    @Transactional
    public void updateJob(SchedulerTask schedulerTask) {
        JobKey jobKey = validateJobKey(schedulerTask.getJobName(), schedulerTask.getJobGroup());
        JobDetail jobDetail = newJobDetail(schedulerTask, jobKey);
        CronTrigger cronTrigger = newCronTrigger(schedulerTask, jobDetail);
        schedulerWrapper.scheduleJob(jobDetail, Sets.newHashSet(cronTrigger), true);
    }

    public void triggerJob(String jobName, String jobGroup) {
        JobKey jobKey = validateJobKey(jobName, jobGroup);
        schedulerWrapper.triggerJob(jobKey);
    }

    public void pauseJob(String jobName, String jobGroup) {
        JobKey jobKey = validateJobKey(jobName, jobGroup);
        schedulerWrapper.pauseJob(jobKey);
    }

    public void resumeJob(String jobName, String jobGroup) {
        JobKey jobKey = validateJobKey(jobName, jobGroup);
        schedulerWrapper.resumeJob(jobKey);
    }

    private JobKey validateJobKey(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (!schedulerWrapper.checkExists(jobKey)) {
            log.error("jobKey not exist, {}", jobKey);
            throw new RuntimeException(String.format("jobKey not exist, jobName: %s, jobGroup: %s",
                jobKey.getName(), jobKey.getGroup()));
        }
        return jobKey;
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

    private JobDetail newJobDetail(SchedulerTask schedulerTask, JobKey jobKey) {
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
            throw new RuntimeException(String.format("job exist, jobName: %s, jobGroupName: %s",
                jobKey.getName(), jobKey.getGroup()));
        }
        return jobKey;
    }
}
