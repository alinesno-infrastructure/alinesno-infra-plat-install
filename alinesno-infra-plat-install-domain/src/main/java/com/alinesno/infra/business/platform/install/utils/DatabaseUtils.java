package com.alinesno.infra.business.platform.install.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.lang.exception.RpcServiceRuntimeException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

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
	 * 创建数据库
	 */
	public static void createDatabase(String databaseName) {
		
//		String dbUrl = Db.Config.get(Const.CONFIG_MYSQL_URL) ;
//		String user = Db.Config.get(Const.CONFIG_MYSQL_USERNAME) ;
//		String pass = Db.Config.get(Const.CONFIG_MYSQL_PASSWORD) ;
//
//		Connection conn = null;
//		Statement stmt = null;
//		try {
//			Class.forName(DRIVER_NAME);
//			log.debug("连接数据库:{}..." , dbUrl);
//
//			conn = DriverManager.getConnection(dbUrl, user, pass);
//
//			log.debug("创建服务[{}]数据库 :[{}] ..." , acp.getAppName() , acp.getDatabaseName());
//			stmt = conn.createStatement();
//
//			String sql = "create database if not exists " + acp.getDatabaseName() + " default character set utf8mb4 collate utf8mb4_unicode_ci;";
//			stmt.executeUpdate(sql);
//
//			log.debug("创建服务[{}]数据库 :[{}] 成功" , acp.getAppName() , acp.getDatabaseName());
//		} catch (Exception e) {
//			log.error("创建服务[{"+acp.getAppName()+"}]数据库["+ acp.getDatabaseName() +"]创建失败");
//			throw new RpcServiceRuntimeException("创建服务[{"+acp.getAppName()+"}]数据库["+ acp.getDatabaseName() +"]创建失败") ;
//		} finally {
//			try {
//				if (stmt != null)
//					stmt.close();
//			} catch (SQLException se2) {
//			} // nothing we can do
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}
//		}
	}

	/**
	 * 导入数据库脚本
	 * 
	 * @param sqlFilename
	 */
	public static void importDatabase(String sqlFilename) {
		
//		// TODO  待优化，只需要1个connect即可
//		String dbUrl = Db.Config.get(Const.CONFIG_MYSQL_URL) ;
//		String user = Db.Config.get(Const.CONFIG_MYSQL_USERNAME) ;
//		String pass = Db.Config.get(Const.CONFIG_MYSQL_PASSWORD) ;
//
//		StringBuffer dbUrlBuffer = new StringBuffer() ;
//		dbUrlBuffer.append(dbUrl) ;
//		dbUrlBuffer.append(acp.getDatabaseName()) ;
//		dbUrlBuffer.append("?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&useSSL=false&serverTimezone=GMT") ;
//
//		log.debug("连接服务[{}]数据库:{} , url:{}" , acp.getAppName() , acp.getDatabaseName() , dbUrlBuffer.toString());
//
//		Connection conn = null;
//		try {
//			Class.forName(DRIVER_NAME);
//			log.debug("连接数据库:{}..." , dbUrl);
//
//			conn = DriverManager.getConnection(dbUrlBuffer.toString() , user, pass);
//
//			Resource rc = new  FileUrlResource(installFile + sqlFilename) ;
//			log.debug("导入服务[{}]数据库:{}" , acp.getAppName() , acp.getDatabaseName());
//			ScriptUtils.executeSqlScript(conn, rc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RpcServiceRuntimeException("导入服务[{"+acp.getAppName()+"}]数据库["+ dbUrl +"]导入失败") ;
//		}finally {
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}
//		}
	}

//	public static void importDatabase(AIP acp , String installFile) {
//		installFile += File.separator + Const.DIR_SQL + File.separator ;
//		importDatabase(acp , acp.getSqlFilename() , installFile) ;
//	}

}
