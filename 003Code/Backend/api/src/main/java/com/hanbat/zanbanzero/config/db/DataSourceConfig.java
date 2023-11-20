package com.hanbat.zanbanzero.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {
    private static final String MASTER = "MASTER";
    private static final String SLAVE = "SLAVE";

    @Bean
    @Qualifier(MASTER)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(SLAVE)
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource dataSourceRouter(@Qualifier(MASTER) DataSource master,
                                        @Qualifier(SLAVE) DataSource slave) {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(MASTER, master);
        dataSourceMap.put(SLAVE, slave);

        dataSourceRouter.setTargetDataSources(dataSourceMap);
        dataSourceRouter.setDefaultTargetDataSource(master);

        return dataSourceRouter;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = dataSourceRouter(masterDataSource(), slaveDataSource());
        // 스프링은 기본적으로 트랜잭션 시작과 동시에 DataSource Connection을 생성한다.
        // 하지만 트랜잭션 시작 시점이 아닌, 쿼리를 실행할 때마다 DataSource를 결정해야 하므로 LazyConnectionDataSourceProxy 사용
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}
