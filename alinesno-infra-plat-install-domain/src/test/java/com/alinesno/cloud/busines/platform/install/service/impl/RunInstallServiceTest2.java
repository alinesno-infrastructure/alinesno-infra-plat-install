package com.alinesno.cloud.busines.platform.install.service.impl;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alinesno.cloud.busines.platform.install.constants.Const;
import com.alinesno.cloud.busines.platform.install.constants.InstallType;
import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallTypeDto;
import com.alinesno.cloud.busines.platform.install.session.Db;
import com.alinesno.cloud.busines.platform.install.utils.NetUtils;

@Slf4j
class RunInstallServiceTest2 {

	@Autowired
	private RunInstallService installService ; 

	@Test
	void testInstallAcp() throws MalformedURLException, IOException {
		

		InstallTypeDto dto = new InstallTypeDto() ; 
		dto.setType(InstallType.K8S.getType()) ; 
		
		// 把配置放到map中
		Db.Config.put(Const.CONFIG_MYSQL_URL , "jdbc:mysql://localhost:3306/") ; 
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, "root") ; 
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD , "adminer") ;
		
		Db.Config.put(Const.CONFIG_REDIS_HOST , "localhost") ; 
		Db.Config.put(Const.CONFIG_REDIS_PORT , "6379") ; 
		Db.Config.put(Const.CONFIG_REDIS_PASSORD , "") ; 
			
		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, "http://minio.bigdata.lbxinhu.linesno.com") ;
		Db.Config.put(Const.CONFIG_MINIO_KEY, "min_ioxinfox@9824yuhutegen") ;
		Db.Config.put(Const.CONFIG_MINIO_SECRET , "min_ioxinfox@9824yuhutegen") ;
		
		Db.Config.put(Const.CONFIG_DOMAIN_HOST , "") ;
		
		installService.installAcp(dto);
		
	}

	@Test
	void testInstallAcpByKubernetes() throws MalformedURLException, IOException {
		InstallTypeDto dto = new InstallTypeDto() ; 
		dto.setType(InstallType.K8S.getType()) ; 
		
		// 把配置放到map中
		Db.Config.put(Const.CONFIG_MYSQL_URL , "jdbc:mysql://localhost:3306/") ; 
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, "root") ; 
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD , "adminer") ;
		
		Db.Config.put(Const.CONFIG_REDIS_HOST , "localhost") ; 
		Db.Config.put(Const.CONFIG_REDIS_PORT , "6379") ; 
		Db.Config.put(Const.CONFIG_REDIS_PASSORD , "") ; 
			
		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, "http://minio.bigdata.lbxinhu.linesno.com") ;
		Db.Config.put(Const.CONFIG_MINIO_KEY, "min_ioxinfox@9824yuhutegen") ;
		Db.Config.put(Const.CONFIG_MINIO_SECRET , "min_ioxinfox@9824yuhutegen") ;
		
		Db.Config.put(Const.CONFIG_DOMAIN_HOST , "") ;
		
		installService.installAcp(dto);
	}

	@SuppressWarnings("unused")
	@Test
	void testDownloadMysqlScript() throws MalformedURLException, IOException {
	
		log.debug("installService = {}" , installService);
		String path = NetUtils.getInstallFile() ; 
		
//		installService.downloadMysqlScript(null);
//		http://data.linesno.com/acp-install/sql/init_sql.sql
//		http://data.linesno.com/acp-install/sql/init_sql.sql
		
	}

	@Test
	void testValidateApplicationStatus() {
		fail("Not yet implemented");
	}

	@Test
	void testGetK8SApplayIngressShell() {
		fail("Not yet implemented");
	}

	@Test
	void testGetK8SApplayShell() {
		fail("Not yet implemented");
	}

	@Test
	void testProcessRecord() {
		fail("Not yet implemented");
	}

	@Test
	void testWriterK8sIngressYaml() {
		fail("Not yet implemented");
	}

	@Test
	void testWriterK8sYaml() {
		fail("Not yet implemented");
	}

	@Test
	void testImportMysqlScript() {
		fail("Not yet implemented");
	}

	@Test
	void testInstallAcpByDockerCompose() throws MalformedURLException, IOException {
		InstallTypeDto dto = new InstallTypeDto() ; 
		dto.setType(InstallType.DOCKER.getType()) ; 
		
		// 把配置放到map中
		Db.Config.put(Const.CONFIG_MYSQL_URL , "jdbc:mysql://localhost:3306/") ; 
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, "root") ; 
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD , "adminer") ;
		
		Db.Config.put(Const.CONFIG_REDIS_HOST , "localhost") ; 
		Db.Config.put(Const.CONFIG_REDIS_PORT , "6379") ; 
		Db.Config.put(Const.CONFIG_REDIS_PASSORD , "") ; 
			
		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, "http://minio.bigdata.lbxinhu.linesno.com") ;
		Db.Config.put(Const.CONFIG_MINIO_KEY, "min_ioxinfox@9824yuhutegen") ;
		Db.Config.put(Const.CONFIG_MINIO_SECRET , "min_ioxinfox@9824yuhutegen") ;
		
		Db.Config.put(Const.CONFIG_DOMAIN_HOST , "") ;
		
		installService.installAcp(dto);

	}

	@Test
	void testGetInstallFile() {
		fail("Not yet implemented");
	}

	@Test
	void testGetRunnerLog() {
		fail("Not yet implemented");
	}

	@Test
	void testGetInstallStatus() {
		fail("Not yet implemented");
	}

	@Test
	void testIsHostConnectable() {
		fail("Not yet implemented");
	}

}
