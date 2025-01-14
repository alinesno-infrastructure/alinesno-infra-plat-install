package com.alinesno.infra.business.platform.install;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动入口
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 不需要加载数据库时
@EnableScheduling
public class AIPInstallApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIPInstallApplication.class, args);
	}
	
}
