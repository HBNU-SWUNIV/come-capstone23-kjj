package com.batch.batch.config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
    @ConfigurationProperties(prefix = "spring.datasource.data")
    public HikariConfig dbHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig batchHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Qualifier("dataDataSource")
    public DataSource dbDataSource() {
        return new HikariDataSource(dbHikariConfig());
    }

    @Bean
    @Primary
    public DataSource batchDataSource() {
        return new HikariDataSource(batchHikariConfig());
    }
}
