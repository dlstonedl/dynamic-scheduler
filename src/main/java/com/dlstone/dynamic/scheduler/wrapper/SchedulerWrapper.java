package com.dlstone.dynamic.scheduler.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class SchedulerWrapper {

    private SchedulerFactoryBean schedulerFactoryBean;

    public SchedulerWrapper(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public List<String> getJobGroupNames() {
        try {
            return schedulerFactoryBean.getScheduler().getJobGroupNames();
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return Collections.emptyList();
    }

    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) {
        try {
            return schedulerFactoryBean.getScheduler().getJobKeys(matcher);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return Collections.emptySet();
    }

    public JobDetail getJobDetail(JobKey jobKey) {
        try {
            return schedulerFactoryBean.getScheduler().getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return null;
    }

    public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) {
        try {
            return schedulerFactoryBean.getScheduler().getTriggersOfJob(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return Collections.emptyList();
    }

    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) {
        try {
            return schedulerFactoryBean.getScheduler().getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return null;
    }

    public boolean checkExists(JobKey jobKey) {
        try {
            return schedulerFactoryBean.getScheduler().checkExists(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return false;
    }

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) {
        try {
            return schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return null;
    }

    public boolean deleteJob(JobKey jobKey) {
        try {
            return schedulerFactoryBean.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
        return false;
    }

    public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) {
        try {
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, triggersForJob, replace);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
    }

    public void triggerJob(JobKey jobKey) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
    }

    public void pauseJob(JobKey jobKey) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            throwRuntimeException(e);
        }
    }

    private void throwRuntimeException(SchedulerException e) {
        log.error("SchedulerWrapper: " + e);
        throw new RuntimeException(e);
    }
}
