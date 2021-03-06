package com.dlstone.dynamic.scheduler.controller;

import com.dlstone.dynamic.scheduler.controller.request.JobRequest;
import com.dlstone.dynamic.scheduler.controller.request.UpdateJobRequest;
import com.dlstone.dynamic.scheduler.model.JobAction;
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
        quartzService.addJob(SchedulerTaskFactory.generateSchedulerTask(jobRequest));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/jobs/job/{jobGroup}/{jobName}")
    public ResponseEntity deleteJob(@PathVariable String jobGroup, @PathVariable String jobName) {
        quartzService.deleteJob(jobName, jobGroup);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/jobs/job/{jobGroup}/{jobName}")
    public ResponseEntity updateJob(@PathVariable String jobGroup,
                                    @PathVariable String jobName,
                                    @RequestBody UpdateJobRequest updateJobRequest) {
        quartzService.updateJob(SchedulerTaskFactory.generateSchedulerTask(jobName, jobGroup, updateJobRequest));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/jobs/job/{jobGroup}/{jobName}/action")
    public ResponseEntity triggerJob(@PathVariable String jobGroup,
                                     @PathVariable String jobName,
                                     @RequestParam JobAction jobAction) {
        if (jobAction == JobAction.EXECUTE) {
            quartzService.triggerJob(jobName, jobGroup);
        } else if (jobAction == JobAction.PAUSE){
            quartzService.pauseJob(jobName, jobGroup);
        } else {
            quartzService.resumeJob(jobName, jobGroup);
        }
        return ResponseEntity.ok().build();
    }
}
