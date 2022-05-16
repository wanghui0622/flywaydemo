package com.wanghowie;

import com.wanghowie.config.FlywayLoad;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author by wanghui03
 * @Classname FlywayApplication
 * @Description TODO
 * @Date 2022/5/16 14:16
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public class FlywayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlywayApplication.class, args);
		FlywayLoad.dbMigrate();
	}

}
