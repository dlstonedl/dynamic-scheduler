package com.dlstone.dynamic.scheduler.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.quartz.JobDetail;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.sql.DataSource;

@Configuration
public class QuartzConfiguration {

    @Bean
    @QuartzDataSource
    public DataSource quartz(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }
}
