package com.dlstone.dynamic.scheduler.service;

import com.dlstone.dynamic.scheduler.mapper.JobHistoryMapper;
import com.dlstone.dynamic.scheduler.model.JobHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobHistoryService {

    private JobHistoryMapper jobHistoryMapper;

    @Autowired
    public JobHistoryService(JobHistoryMapper jobHistoryMapper) {
        this.jobHistoryMapper = jobHistoryMapper;
    }

    public List<JobHistory> findJobHistories(String jobName, String jobGroup) {
        return jobHistoryMapper.findJobHistories(jobName, jobGroup);
    }
}
