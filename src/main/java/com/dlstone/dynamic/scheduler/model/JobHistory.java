package com.dlstone.dynamic.scheduler.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobHistory {
    private String id;
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private LocalDateTime jobStartTime;
    private LocalDateTime jobEndTime;
    private long jobDuration;
    private String jobStatus;
    private String jobException;
}
