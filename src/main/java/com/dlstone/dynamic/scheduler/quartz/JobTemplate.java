package com.dlstone.dynamic.scheduler.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class JobTemplate extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        String url = mergedJobDataMap.getString("url");

        RestTemplate restTemplate = new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory())
            .build();
        restTemplate.postForObject(url, null, Object.class);
    }

}
