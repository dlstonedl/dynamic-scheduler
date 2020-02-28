package com.dlstone.dynamic.scheduler.controller.request;

import javax.validation.constraints.NotNull;

public class UpdateJobRequest {
    public String jobDescription;
    @NotNull
    public String url;
    @NotNull
    public String cronExpression;
}
