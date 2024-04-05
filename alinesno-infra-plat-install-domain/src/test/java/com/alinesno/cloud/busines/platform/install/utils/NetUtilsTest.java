package com.alinesno.cloud.busines.platform.install.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alinesno.cloud.busines.platform.install.constants.AIP;
import com.alinesno.cloud.busines.platform.install.constants.Const;

class NetUtilsTest {
	
	private static final Logger log = LoggerFactory.getLogger(NetUtilsTest.class) ; 

	@Test
	void testDownload() throws IOException {
		
		String userHomeDir = System.getProperty("user.home");
		String url = Const.qiniuSqlDomain +  AIP.alinesno_cloud_initializr_admin.getSqlFilename() ; 

		log.debug("The User Home Directory is %s", userHomeDir);
		
		String installPath = userHomeDir + File.separator + ".acp-install" ; 
		File installFile = new File(installPath) ; 
		
		if(!installFile.exists()) {
			FileUtils.forceMkdir(new File(installPath));
		}
		
		installPath += File.separator + NetUtils.filename(url) ;
		FileUtils.forceDelete(new File(installPath));  // 删除本地文件
		
		NetUtils.download(url , installPath) ;
		
	}
	
	@Test
	void testFilename() {
		
		String url = "http://data.linesno.com/acp-install/sql/dev_alinesno_cloud_base_initial_v212.sql" ; 
		
		String filename = NetUtils.filename(url) ;
		
		log.debug("filename = {}" , filename);
	}
	
	@Test
	void testDownloadK8SYaml() throws IOException {
		
		String userHomeDir = System.getProperty("user.home");
		String installPath = userHomeDir + File.separator + ".acp-install" ; 
		
		String url = "http://data.linesno.com/acp-install/kubernetes/k8s_base_template.vtl" ; 
		
		NetUtils.downloadK8SYaml(installPath , url); 
		
	}

}
