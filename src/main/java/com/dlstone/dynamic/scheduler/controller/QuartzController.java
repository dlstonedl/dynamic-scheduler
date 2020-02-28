package com.dlstone.dynamic.scheduler.controller;

import com.dlstone.dynamic.scheduler.controller.request.JobRequest;
import com.dlstone.dynamic.scheduler.model.SchedulerTask;
import com.dlstone.dynamic.scheduler.model.SchedulerTaskFactory;
import com.dlstone.dynamic.scheduler.service.QuartzService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartz")
public class QuartzController {

    private QuartzService quartzService;

    public QuartzController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    @GetMapping("/jobs")
    public List<SchedulerTask> getAllSchedulerTasks() {
        return quartzService.getAllSchedulerTasks();
    }

    @PostMapping("/jobs")
    public ResponseEntity addJob(@RequestBody JobRequest jobRequest) {
        SchedulerTask schedulerTask = SchedulerTaskFactory.generateSchedulerTask(jobRequest);
        quartzService.addJob(schedulerTask);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/jobs/{jobGroup}/{jobName}")
    public ResponseEntity deleteJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        quartzService.deleteJob(jobGroup, jobName);
        return ResponseEntity.ok().build();
    }
}
