package com.batch.batch.config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Qualifier("dataDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.data")
    public DataSource dbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }
}
