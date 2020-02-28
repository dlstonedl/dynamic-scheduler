package com.dlstone.dynamic.scheduler.model;

import lombok.Data;

@Data
public class JobHistory {
    private String id;
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private String jobStartTime;
    private String jobEndTime;
    private int jobDuration;
    private String jobStatus;
    private String jobException;
}
