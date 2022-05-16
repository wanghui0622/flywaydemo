package com.wanghowie.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author by wanghui03
 * @Classname DatabaseConfig
 * @Description TODO
 * @Date 2022/5/16 14:16
 */

@Component
@Configuration
@ConfigurationProperties(prefix = "spring")
public class DatabaseConfig {

    @Bean(name = "secondDb")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    private Map<String, Map<String,String>> datasource;

    public Map<String, Map<String, String>> getDatasource() {
    return datasource;
    }

    public void setDatasource(Map<String, Map<String, String>> datasource) {
    this.datasource = datasource;
    }

    public DatabaseConfig() {
    }

    public DatabaseConfig(Map<String, Map<String, String>> datasource) {
            this.datasource = datasource;
    }

}
