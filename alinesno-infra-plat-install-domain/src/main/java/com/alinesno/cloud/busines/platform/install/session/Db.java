package com.alinesno.cloud.busines.platform.install.session;

import java.util.HashMap;
import java.util.Map;

import com.alinesno.cloud.busines.platform.install.constants.Const;

/**
 * 一个简易的数据存储空间
 */
public class Db {

	/**
	 * 安装方式
	 */
	public static String installType = null;

	/**
	 * 安装模型
	 */
	public static String modelType = null;

	/**
	 * 当前可用配置
	 */
	public static Map<String, String> Config = new HashMap<String, String>();

	/**
	 * 当前进度情况
	 */
	public static Map<String, Integer> InstallProcess = new HashMap<String, Integer>();

	static {
		// 初始化步骤数据
		initInstallProcess() ; 

		// 初始化当前应用配置
		Db.Config.put(Const.CONFIG_MYSQL_URL, null);
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, null);
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD, null);

		Db.Config.put(Const.CONFIG_REDIS_HOST, null);
		Db.Config.put(Const.CONFIG_REDIS_PORT, null);
		Db.Config.put(Const.CONFIG_REDIS_PASSORD, null);

		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, null);
		Db.Config.put(Const.CONFIG_MINIO_KEY, null);
		Db.Config.put(Const.CONFIG_MINIO_SECRET, null);

		Db.Config.put(Const.CONFIG_DOMAIN_HOST, null);
	}

	/**
	 * 重新初始化应用程序
	 */
	public static void initInstallProcess() {
		InstallProcess.put(Const.STEP_1, Const.PRE);
		InstallProcess.put(Const.STEP_2, Const.PRE);
		InstallProcess.put(Const.STEP_3, Const.PRE);
		InstallProcess.put(Const.STEP_4, Const.PRE);
		InstallProcess.put(Const.STEP_5, Const.PRE);
	}

	/**
	 * 是否安装结束，这里只要保留有异常和正常完成的，都属于安装结束
	 */
	public static int isInstallFinish() {
		int status = Const.PRE ; 
	
		for(String key : InstallProcess.keySet()) {
			int value = InstallProcess.get(key) ; 
			
			if(value == Const.ERROR) {
				status = Const.ERROR ; // 异常记录
				break ; 
			}
			
			if(value == Const.SUCCESS) {
				status = Const.SUCCESS ; // 正常记录
				break ; 
			}
			
		}
		
		
		return status ; 
	}

}
