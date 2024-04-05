package com.alinesno.cloud.busines.platform.install.service.impl;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.minio.MinioClient;

class PreConfigServiceImplTest {
	
	private static final Logger log = LoggerFactory.getLogger(PreConfigServiceImplTest.class) ; 

	/**
	 * 验证minio配置
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	@Test
	void testValidateMinio() throws Exception {

		String minioUrl = "http://minio.bigdata.lbxinhu.linesno.com" ;
		String minioUser = "min_ioxinfox@9824yuhutegen" ;
		String minioPwd = "min_ioxinfox@9824yuhutegen" ;
		String testBucket = "alinesno-test-bucket" ;

		MinioClient minioClient = new MinioClient(minioUrl, minioUser, minioPwd);
		log.debug("minioClient = {}" , minioClient);
		
		boolean isExist = minioClient.bucketExists(testBucket);
        if (!isExist) {
            minioClient.makeBucket(testBucket);
        }
	}
	
	@Test
	void testCheckEnvInfo() {
		
	}

}
