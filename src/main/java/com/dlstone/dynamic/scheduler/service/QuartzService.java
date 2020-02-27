package com.dlstone.dynamic.scheduler.service;

import com.dlstone.dynamic.scheduler.model.SchedulerTask;
import com.dlstone.dynamic.scheduler.model.SchedulerTaskFactory;
import com.dlstone.dynamic.scheduler.wrapper.SchedulerWrapper;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuartzService {

    private SchedulerWrapper schedulerWrapper;

    @Autowired
    public QuartzService(SchedulerWrapper schedulerWrapper) {
        this.schedulerWrapper = schedulerWrapper;
    }

    public List<SchedulerTask> getAllSchedulerTasks() {
        return schedulerWrapper.getJobGroupNames().stream()
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
}
