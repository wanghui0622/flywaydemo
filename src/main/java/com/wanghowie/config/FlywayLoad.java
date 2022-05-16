package com.wanghowie.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author by wanghui03
 * @Classname FlywayLoad
 * @Description TODO
 * @Date 2022/5/16 14:18
 */
@Configuration
@EnableTransactionManagement
public class FlywayLoad {
    @Resource
    private DatabaseConfig db_temp;

    public static FlywayLoad flywayLoad;
    public static DatabaseConfig db;
    @PostConstruct
    public void init() {
        db = db_temp;
        flywayLoad = this;
    }

    public static void dbMigrate(){
        Map<String, Map<String, String>> datasource = db.getDatasource();
        datasource.forEach((Key,value)->{
            Flyway flyway = Flyway.configure()
                    .locations("one".equals(Key) ? "/db/migration/one" : "/db/migration/two")
//                    .locations("classpath:db/migration/one")
                    .dataSource(value.get("url"),
                    value.get("username"),
                    value.get("password"))
                    .baselineOnMigrate(true)
                    .validateOnMigrate(false)
                    .load();
            flyway.migrate();
        });
    }
}
