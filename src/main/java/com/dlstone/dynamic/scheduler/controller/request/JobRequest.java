package com.dlstone.dynamic.scheduler.controller.request;

import javax.validation.constraints.NotNull;

public class JobRequest {
    @NotNull
    public String jobName;
    public String jobGroupName;
    public String jobDescription;
    @NotNull
    public String url;
    @NotNull
    public String cronExpression;
}
