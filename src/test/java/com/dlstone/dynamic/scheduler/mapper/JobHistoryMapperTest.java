package com.dlstone.dynamic.scheduler.mapper;

import com.dlstone.dynamic.scheduler.DbTestBase;
import com.dlstone.dynamic.scheduler.model.JobHistory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        jobHistoryMapper.insertJobHistory(jobHistory);
        assertThat(jobHistory.getId()).isNotEmpty();
    }

}
