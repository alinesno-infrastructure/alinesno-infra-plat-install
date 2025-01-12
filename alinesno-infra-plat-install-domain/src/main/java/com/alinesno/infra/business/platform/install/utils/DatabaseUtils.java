package com.alinesno.infra.business.platform.install.utils;

import com.alinesno.infra.business.platform.install.dto.InstallForm;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库工具类
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
public class DatabaseUtils {

	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver" ;

	/**
	 * 导入数据库脚本
	 * 
	 */
	public static void getConnectDatabase(String sqlFilename) {
		
		String dbUrl = "jdbc:mysql://localhost:13306/alinesno_database" ;
		String user = "root";
		String pass = "aip@mysql" ;

		StringBuffer dbUrlBuffer = new StringBuffer() ;
		dbUrlBuffer.append(dbUrl) ;
		dbUrlBuffer.append("?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&serverTimezone=GMT&useSSL=false&autoReconnect=true&failOverReadOnly=false&maxReconnects=10") ;

		log.debug("连接服务数据库url:{}" , dbUrlBuffer) ;

		Connection conn = null;
		try {
			Class.forName(DRIVER_NAME);
			log.debug("连接数据库:{}..." , dbUrl);

			conn = DriverManager.getConnection(dbUrlBuffer.toString() , user, pass);

			Resource rc = new FileUrlResource(sqlFilename) ;
			log.debug("导入服务数据库:{}" , sqlFilename);

			ScriptUtils.executeSqlScript(conn, rc);

		} catch (Exception e) {
			log.error("连接数据库异常" , e);
			throw new RuntimeException("连接数据库异常") ;
		}finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("关闭数据库连接异常" , e);
				}
			}
		}

	}

	public static void initDatabase(InstallForm installForm, List<Project> projectYamlList) {


		try{
			for(Project project : projectYamlList){

				String sqlFilename = project.getDatabaseSqlPath() ;
				if(StringUtils.isNotEmpty(sqlFilename)){
					getConnectDatabase(sqlFilename) ;
					Thread.sleep(1000) ;
				}
			}
		}catch(Exception e){
			log.error("导入数据库异常" , e);
			throw new RuntimeException("连接数据库异常") ;
		}

	}

//	@SneakyThrows
//	public static void main(String[] args) {
//		Connection con =  getConnectDatabase() ;
//		System.out.println("connection = " + con.isValid(10000));
//	}

}
