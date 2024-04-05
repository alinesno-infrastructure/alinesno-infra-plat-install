package com.alinesno.cloud.busines.platform.install.constants;

/**
 * 应用信息
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public enum AIP {

	// 研发体系
	alinesno_cloud_initializr_admin("代码生成器服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_initial_v212.sql" , "dev_alinesno_cloud_base_initial_v212"),  // 代码生成器
	alinesno_cloud_authority("权限资源引擎服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_authority.sql" , "dev_alinesno_cloud_base_authority"), // 资源引擎服务
	alinesno_cloud_platform("云门户管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_cloud_v212.sql" , "dev_alinesno_cloud_base_cloud_v212"), // 云平台服务
	alinesno_cloud_gateway("网关管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_gateway.sql" , "dev_alinesno_cloud_base_gateway"), // 网关服务
	linesno_cloud_message("分布式消息管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_message.sql" , "dev_alinesno_cloud_base_message"), // 消息服务
	alinesno_cloud_base_notice("通知管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_notice.sql" , "dev_alinesno_cloud_base_notice"), // 通知服务
	alinesno_cloud_base_storage("存储管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_storage.sql" , "dev_alinesno_cloud_base_storage"), // 存储服务
	alinesno_cloud_base_workflow("工作流管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_flowable_plus.sql" , "dev_alinesno_cloud_base_flowable_plus"), // 工作流服务
	alinesno_cloud_platform_sso("单点登陆管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_base_sso_v212.sql" , "dev_alinesno_cloud_base_sso_v212"), // 单点登陆服务
	
	alinesno_cloud_cms("内容管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_cms_v212.sql" , "dev_alinesno_cloud_cms_v212"), // 内容服务
	alinesno_cloud_member("会员管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_fuint_v212.sql" , "dev_alinesno_cloud_fuint_v212"),  // 会员服务
	alinesno_cloud_shop("电商管理服务" , "127.0.0.1",3306 , "dev_alinesno_cloud_shop_v212.sql" , "dev_alinesno_cloud_shop_v212"), // 电商服务

	// 监控体系
	alinesno_cloud_logger("127.0.0.1",3306),
	alinesno_cloud_operation("127.0.0.1",3306),
	alinesno_cloud_monitor("127.0.0.1",3306),

	// 数据体系
	alinesno_cloud_data_studio("127.0.0.1",3306),
	alinesno_cloud_data_report("127.0.0.1",3306),
	alinesno_cloud_data_mdn("127.0.0.1",3306),
	alinesno_cloud_data_etl("127.0.0.1",3306),
	alinesno_cloud_data_develop("127.0.0.1",3306) ;

	private String appName ; 
	private String host ; 
	private int port ; 
	private String sqlFilename ; 
	private String databaseName ; 
	
	AIP(String appName , String host , int port , String sqlFilename , String databaseName){
		this.appName = appName ; 
		this.host = host ; 
		this.port = port ; 
		this.sqlFilename = sqlFilename ; 
		this.databaseName = databaseName ; 
	}
	
	
	AIP(String host , int port , String sqlFilename , String databaseName){
		this.host = host ; 
		this.port = port ; 
		this.sqlFilename = sqlFilename ; 
		this.databaseName = databaseName ; 
	}
	
	AIP(String host , int port , String sqlFilename){
		this.host = host ; 
		this.port = port ; 
		this.sqlFilename = sqlFilename ; 
	}

	AIP(String host , int port){
		this.host = host ; 
		this.port = port ; 
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getHost() {
		return host;
	}

	public String getSqlFilename() {
		return sqlFilename;
	}


	public String getAppName() {
		return appName;
	}


	public int getPort() {
		return port;
	}
	
}
