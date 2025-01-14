package com.alinesno.infra.business.platform.install.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alinesno.infra.business.platform.install.dto.InstallForm;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class DatabaseUtils {

	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static volatile DruidDataSource dataSource; // 使用 volatile 关键字确保多线程环境下的可见性

	/**
	 * 初始化 Druid 数据源（懒加载方式）
	 */
	private static synchronized void initDataSource() {
		if (dataSource == null) {
			dataSource = new DruidDataSource();
			try {
				// 设置数据库连接信息
				dataSource.setDriverClassName(DRIVER_NAME);
				dataSource.setUrl("jdbc:mysql://localhost:13306/alinesno_database?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&serverTimezone=GMT&autoReconnect=true&failOverReadOnly=false&maxReconnects=20");
				dataSource.setUsername("root");
				dataSource.setPassword("aip@mysql");

				// 配置初始化大小、最小、最大
				dataSource.setInitialSize(1);
				dataSource.setMinIdle(1);
				dataSource.setMaxActive(20);

				// 配置获取连接等待超时的时间
				dataSource.setMaxWait(60000);

				// 测试是否可以获取连接
				dataSource.getConnection().close();
				log.info("成功初始化 Druid 数据源。");

			} catch (Exception e) {
				log.error("初始化 Druid 数据源失败", e);
				throw new RuntimeException("初始化 Druid 数据源失败", e);
			}
		}
	}

	/**
	 * 获取 Druid 数据源中的连接
	 */
	private static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			initDataSource();
		}
		return dataSource.getConnection();
	}

	/**
	 * 使用 Druid 数据源导入数据库脚本
	 */
	public static void getConnectDatabase(String sqlFilename) {
		Connection conn = null;
		try {
			conn = getConnection(); // 从已有的数据源中获取连接
			Resource rc = new FileUrlResource(sqlFilename);
			log.debug("导入服务数据库:{}", sqlFilename);

			ScriptUtils.executeSqlScript(conn, rc);

		} catch (Exception e) {
			log.error("连接数据库异常", e);
			throw new RuntimeException("连接数据库异常", e);
		} finally {
			if (conn != null) {
				try {
					conn.close(); // 返回连接到连接池而不是关闭它
				} catch (SQLException e) {
					log.error("关闭数据库连接异常", e);
				}
			}
		}
	}

	/**
	 * 执行批量数据库初始化
	 */
	public static void initDatabase(InstallForm installForm, List<Project> projectYamlList) {
		try {
			for (Project project : projectYamlList) {

				log.debug("导入服务:{} , 数据库:{}", project.getDesc() + ":" + project.getName(), project.getDatabase());

				String sqlFilename = project.getDatabaseSqlPath();
				if (StringUtils.isNotEmpty(sqlFilename)) {
					getConnectDatabase(sqlFilename);
					Thread.sleep(1000); // 如果有需要的话，保持这个休眠时间
				}
			}
		} catch (Exception e) {
			log.error("导入数据库异常", e);
			throw new RuntimeException("连接数据库异常", e);
		}
	}

}