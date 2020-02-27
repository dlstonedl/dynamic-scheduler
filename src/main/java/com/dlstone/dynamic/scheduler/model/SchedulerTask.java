package com.dlstone.dynamic.scheduler.model;

import lombok.Data;

@Data
public class SchedulerTask {
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobDescription;
    private String jobTriggerCronExpression;
    private String jobTriggerName;
    private String jobTriggerGroupName;
    private String jobTriggerDescription;
}
