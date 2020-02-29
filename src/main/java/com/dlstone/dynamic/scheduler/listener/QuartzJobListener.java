package com.dlstone.dynamic.scheduler.listener;

import com.dlstone.dynamic.scheduler.mapper.JobHistoryMapper;
import com.dlstone.dynamic.scheduler.model.JobHistory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
public class QuartzJobListener implements JobListener {

    private JobHistoryMapper jobHistoryMapper;

    @Autowired
    public QuartzJobListener(JobHistoryMapper jobHistoryMapper) {
        this.jobHistoryMapper = jobHistoryMapper;
    }

    @Override
    public String getName() {
        return "QuartzJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobHistory jobHistory = new JobHistory();
        JobKey jobKey = context.getJobDetail().getKey();
        jobHistory.setJobName(jobKey.getName());
        jobHistory.setJobGroup(jobKey.getGroup());
        TriggerKey triggerKey = context.getTrigger().getKey();
        jobHistory.setTriggerName(triggerKey.getName());
        jobHistory.setTriggerGroup(triggerKey.getGroup());
        jobHistory.setJobStartTime(LocalDateTime.now());


        jobHistoryMapper.insertJobHistory(jobHistory);
        context.put("id", jobHistory.getId());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setId(String.valueOf(context.get("id")));
        jobHistory.setJobEndTime(LocalDateTime.now());
        jobHistory.setJobDuration(context.getJobRunTime());
        jobHistory.setJobStatus(Objects.isNull(jobException) ? "success" : "failed");
        jobHistory.setJobException(Objects.isNull(jobException) ? null : jobException.getMessage());
        //validate
        TriggerKey triggerKey = context.getTrigger().getKey();
        jobHistory.setTriggerName(triggerKey.getName());
        jobHistory.setTriggerGroup(triggerKey.getGroup());
        jobHistoryMapper.updateJonHistory(jobHistory);
    }
}
