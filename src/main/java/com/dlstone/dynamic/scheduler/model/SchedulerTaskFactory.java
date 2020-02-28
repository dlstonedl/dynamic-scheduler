package com.dlstone.dynamic.scheduler.model;

import com.dlstone.dynamic.scheduler.controller.request.JobRequest;
import com.dlstone.dynamic.scheduler.controller.request.UpdateJobRequest;
import com.dlstone.dynamic.scheduler.wrapper.SchedulerWrapper;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public class SchedulerTaskFactory {

    public static final String URL = "url";

    public static SchedulerTask generateSchedulerTask(SchedulerWrapper schedulerWrapper, JobDetail jobDetail, Trigger trigger) {
        SchedulerTask schedulerTask = new SchedulerTask();

        schedulerTask.setJobName(jobDetail.getKey().getName());
        schedulerTask.setJobGroup(jobDetail.getKey().getGroup());
        schedulerTask.setJobDescription(jobDetail.getDescription());
        schedulerTask.setJobDataMap(jobDetail.getJobDataMap());
        schedulerTask.setJobTriggerName(trigger.getKey().getName());
        schedulerTask.setJobTriggerGroupName(trigger.getKey().getGroup());
        schedulerTask.setJobTriggerDescription(trigger.getDescription());
        schedulerTask.setJobStatus(schedulerWrapper.getTriggerState(trigger.getKey()).name());

        if (trigger instanceof CronTrigger){
            CronTrigger cronTrigger = (CronTrigger) trigger;
            schedulerTask.setJobTriggerCronExpression(cronTrigger.getCronExpression());
        }
        return schedulerTask;
    }

    public static SchedulerTask generateSchedulerTask(JobRequest jobRequest) {
        SchedulerTask schedulerTask = new SchedulerTask();
        schedulerTask.setJobName(jobRequest.jobName);
        schedulerTask.setJobGroup(jobRequest.jobGroupName);
        schedulerTask.setJobDescription(jobRequest.jobDescription);
        schedulerTask.setJobTriggerCronExpression(jobRequest.cronExpression);
        schedulerTask.setJobTriggerName(jobRequest.jobName);
        schedulerTask.setJobTriggerGroupName(jobRequest.jobGroupName);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(URL, jobRequest.url);
        schedulerTask.setJobDataMap(jobDataMap);

        return schedulerTask;
    }

    public static SchedulerTask generateSchedulerTask(String jobName, String jobGroup, UpdateJobRequest updateJobRequest) {
        SchedulerTask schedulerTask = new SchedulerTask();

        schedulerTask.setJobName(jobName);
        schedulerTask.setJobGroup(jobGroup);
        schedulerTask.setJobDescription(updateJobRequest.jobDescription);
        schedulerTask.setJobTriggerCronExpression(updateJobRequest.cronExpression);
        schedulerTask.setJobTriggerName(jobName);
        schedulerTask.setJobTriggerGroupName(jobGroup);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(URL, updateJobRequest.url);
        schedulerTask.setJobDataMap(jobDataMap);

        return schedulerTask;
    }

}
