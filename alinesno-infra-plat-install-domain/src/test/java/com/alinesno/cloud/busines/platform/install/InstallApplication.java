package com.alinesno.cloud.busines.platform.install;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动入口
 *
 * @author ${author} ${authorEmail}
 * @since 2020-12-08 10:12:366
 */
// @EnableRpc
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)  // 不需要加载数据库时
public class InstallApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstallApplication.class, args);
	}

}

