package com.dlstone.dynamic.scheduler.mapper;

import com.dlstone.dynamic.scheduler.model.JobHistory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobHistoryMapper {

    @Insert("insert into job_history(job_name, job_group, trigger_name, trigger_group, " +
        "job_start_time, job_end_time, job_duration, job_status, job_exception) " +
        "values(#{jobName}, #{jobGroup}, #{triggerName}, #{triggerGroup}, #{jobStartTime}, " +
        "#{jobEndTime}, #{jobDuration}, #{jobStatus}, #{jobException})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertJobHistory(JobHistory jobHistory);

    @Select("select * from job_history where job_name = #{jobName} and job_group = #{jobGroup} order by id desc limit 10")
    List<JobHistory> findJobHistories(@Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

    @Select("select * from job_history where job_name = #{jobName} and job_group = #{jobGroup} order by id desc limit 1")
    JobHistory findJobHistory(@Param("jobName") String jobName, @Param("jobGroup") String jobGroup);
}
