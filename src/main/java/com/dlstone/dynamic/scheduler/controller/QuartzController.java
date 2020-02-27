package com.dlstone.dynamic.scheduler.controller;

import com.dlstone.dynamic.scheduler.model.SchedulerTask;
import com.dlstone.dynamic.scheduler.service.QuartzService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
