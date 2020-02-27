package com.dlstone.dynamic.scheduler.model;

import com.dlstone.dynamic.scheduler.wrapper.SchedulerWrapper;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public class SchedulerTaskFactory {

    public static SchedulerTask generateSchedulerTask(SchedulerWrapper schedulerWrapper, JobDetail jobDetail, Trigger trigger) {
        SchedulerTask schedulerTask = new SchedulerTask();

        schedulerTask.setJobName(jobDetail.getKey().getName());
        schedulerTask.setJobGroup(jobDetail.getKey().getGroup());
        schedulerTask.setJobDescription(jobDetail.getDescription());
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
}
