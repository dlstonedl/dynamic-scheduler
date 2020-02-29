package com.dlstone.dynamic.scheduler.mapper;

import com.dlstone.dynamic.scheduler.DbTestBase;
import com.dlstone.dynamic.scheduler.model.JobHistory;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class JobHistoryMapperTest extends DbTestBase {

    @Autowired
    private JobHistoryMapper jobHistoryMapper;

    @Test
    public void should_insert_job_history() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setJobName("jobName");
        jobHistory.setJobGroup("jobGroup");
        jobHistory.setTriggerName("triggerName");
        jobHistory.setTriggerGroup("triggerGroup");
        jobHistory.setJobStartTime(LocalDateTime.now());
        jobHistoryMapper.insertJobHistory(jobHistory);
        assertThat(jobHistory.getId()).isNotEmpty();
    }

    @Test
    @DatabaseSetup("/db/job_history.xml")
    public void should_return_job_history() {
        JobHistory jobHistory = jobHistoryMapper.findJobHistory("jobName", "jobGroup");
        assertThat(jobHistory).isNotNull();
    }

    @Test
    @DatabaseSetup("/db/job_history.xml")
    public void should_update_job_history() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setId("0");
        jobHistory.setJobEndTime(LocalDateTime.now());
        jobHistoryMapper.updateJonHistory(jobHistory);
        JobHistory newJobHistory = jobHistoryMapper.findJobHistory("jobName", "jobGroup");
        assertThat(newJobHistory.getJobEndTime()).isNotNull();
    }

}