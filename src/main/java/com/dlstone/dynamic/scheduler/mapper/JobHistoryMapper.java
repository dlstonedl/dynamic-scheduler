package com.dlstone.dynamic.scheduler.mapper;

import com.dlstone.dynamic.scheduler.model.JobHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JobHistoryMapper {

    @Insert("insert into job_history(job_name, job_group, trigger_name, trigger_group, " +
        "job_start_time, job_end_time, job_duration, job_status, job_exception) " +
        "values(#{jobName}, #{jobGroup}, #{triggerName}, #{triggerGroup}, #{jobStartTime}, " +
        "#{jobEndTime}, #{jobDuration}, #{jobStatus}, #{jobException})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertJobHistory(JobHistory jobHistory);

}
