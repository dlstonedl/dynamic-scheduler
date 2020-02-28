package com.dlstone.dynamic.scheduler.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

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

    @Bean
    public PlatformTransactionManager transactionManager(DataSource quartz) {
          return new DataSourceTransactionManager(quartz);
    }
}
