package com.alinesno.infra.business.platform.install.constants;

/**
 * 常用变量
 * @version 1.0.0
 */
public interface Const {

	String DEFAULT_SSE_CHANNEL = "9527";

	/**
	 * 下载地址
	 */
	String qiniuDomain = "http://data.linesno.com/aip-install/";

//	String DIR_K8S = "kubernetes" ;
//	String DIR_DOCKER = "docker" ;
//	String DIR_SQL = "sql" ;
//
//	String qiniuSqlDomain = qiniuDomain + DIR_SQL + "/";
//	String qiniuDockerDomain = qiniuDomain + DIR_DOCKER + "/" ;
//	String qiniuK8sDomain = qiniuDomain + DIR_K8S + "/" ;
//
//	/**
//	 * 配置变量
//	 */
//	String CONFIG_MYSQL_URL = "config_mysql_url";
//	String CONFIG_MYSQL_USERNAME = "config_mysql_username";
//	String CONFIG_MYSQL_PASSWORD = "config_mysql_password";
//
//	String CONFIG_REDIS_HOST = "config_redis_host";
//	String CONFIG_REDIS_PORT = "config_redis_port";
//	String CONFIG_REDIS_PASSORD = "config_redis_passord";
//
//	String CONFIG_MINIO_ENDPOINT = "config_minio_endpoint";
//	String CONFIG_MINIO_KEY = "config_minio_key";
//	String CONFIG_MINIO_SECRET = "config_minio_secret";
//
//	String CONFIG_DOMAIN_HOST = "config_domain_host";
//
//	/**
//	 * 安装步骤
//	 */
//	String STEP_1 = "download_sql_script"; // 下载sql脚本
//	String STEP_2 = "import_sql_script"; // 导入sql脚本
//	String STEP_3 = "download_k8s_yaml"; // 下载k8s.yaml文件
//	String STEP_4 = "download_docker_images"; // 下载镜像
//	String STEP_5 = "acp_install"; // 安装acp
//
//	/**
//	 * 安装的文字
//	 *
//	 * @author luoxiaodong
//	 * @version 1.0.0
//	 */
//	public enum stepText {
//
//		STEP_1(Const.STEP_1, "下载数据库脚本"), // 下载sql脚本
//		STEP_2(Const.STEP_2, "初始化数据库"), // 导入sql脚本
//		STEP_3(Const.STEP_3, "下载安装脚本"), // 下载k8s.yaml文件
//		STEP_4(Const.STEP_4, "初始化应用程序"), // 下载镜像
//		STEP_5(Const.STEP_5, "验证启动情况"); // 安装acp
//
//		private String step;
//		private String text;
//
//		stepText(String step, String text) {
//			this.step = step;
//			this.text = text;
//		}
//
//		public String getStep() {
//			return step;
//		}
//
//		public String getText() {
//			return text;
//		}
//
//	}
//
//	/**
//	 * 安装状态
//	 */
//	int PRE = 0;  // 待安装
//	int SUCCESS = 1;  // 安装成功
//	int DOING = 2;  // 安装中
//	int ERROR = 9;  // 安装异常
//
//	/**
//	 * 下载k8s初始化文件名
//	 */
//	String SQL_SCRIPT = "init_sql.sql";
//
//	String K8S_TEMPLATE_YAML = "k8s_template.yaml";
//	String K8S_INGRESS_TEMPLATE_YAML = "k8s_ingress_template.yaml";
//	String K8S_YAML = "k8s.yaml";
//	String K8S_INGRESS_YAML = "kys_ingress.yaml";
//
//	/**
//	 * 下载docker初始化文件名
//	 */
//	String DOCKER_TEMPLATE_YAML = "docker-compose_template.yaml";
//	String DOCKER_YAML = "docker-compose.yaml";

}
