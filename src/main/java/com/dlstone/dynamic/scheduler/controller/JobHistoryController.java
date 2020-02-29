package com.dlstone.dynamic.scheduler.controller;

import com.dlstone.dynamic.scheduler.model.JobHistory;
import com.dlstone.dynamic.scheduler.service.JobHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/history")
public class JobHistoryController {

    private JobHistoryService jobHistoryService;

    @Autowired
    public JobHistoryController(JobHistoryService jobHistoryService) {
        this.jobHistoryService = jobHistoryService;
    }

    @GetMapping("/jobs/{jobGroup}/{jobName}")
    public List<JobHistory> findJobHistories(@PathVariable String jobGroup,
                                             @PathVariable String jobName) {
        return jobHistoryService.findJobHistories(jobName, jobGroup);
    }

}
