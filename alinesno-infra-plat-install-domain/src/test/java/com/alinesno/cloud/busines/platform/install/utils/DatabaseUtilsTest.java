package com.alinesno.cloud.busines.platform.install.utils;

import org.junit.jupiter.api.Test;

import com.alinesno.cloud.busines.platform.install.constants.Const;
import com.alinesno.cloud.busines.platform.install.session.Db;

class DatabaseUtilsTest {

	static {
	
		String url = "jdbc:mysql://localhost:3306/" ;
		String username = "root" ; 
		String password = "adminer" ; 
		
		Db.Config.put(Const.CONFIG_MYSQL_URL , url) ; 
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, username) ; 
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD , password) ; 
	}
	
	
	@Test
	void testCreateDatabase() {
		String dbName = "test_auto_create_1" ; 

//		DatabaseUtils.createDatabase(dbName);
	}

	@Test
	void testImportDatabase() {
	
		String fileFole = "/Users/luodong/.acp-install/" ; 
		String scriptName = "dev_alinesno_cloud_base_initial_v212.sql" ; 
		String dbName = "test_auto_create_1" ; 
		
//		DatabaseUtils.importDatabase(dbName , scriptName , fileFole);
	}

}
