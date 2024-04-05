//package com.alinesno.cloud.busines.platform.install.gateway.rest;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alinesno.cloud.busines.platform.install.constants.Const;
//import com.alinesno.cloud.busines.platform.install.constants.InstallType;
//import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallTypeDto;
//import com.alinesno.cloud.busines.platform.install.service.impl.RunInstallService;
//import com.alinesno.cloud.busines.platform.install.session.Db;
//import com.alinesno.cloud.common.facade.response.AjaxResult;
//
///**
// * 测试安装
// * 
// * @author luoxiaodong
// * @since 2022年8月9日 上午6:23:43
// */
//@RestController
//@RequestMapping("/api/install/demo")
//public class DemoRest {
//
//
//	@Autowired
//	private RunInstallService installService ; 
//
//	@GetMapping("/testInstall")
//	public AjaxResult testInstall() throws MalformedURLException, IOException {
//		InstallTypeDto dto = new InstallTypeDto() ; 
//		dto.setType(InstallType.K8S.getType()) ; 
//		
//		// 把配置放到map中
//		Db.Config.put(Const.CONFIG_MYSQL_URL , "jdbc:mysql://localhost:3306/") ; 
//		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, "root") ; 
//		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD , "adminer") ;
//		
//		Db.Config.put(Const.CONFIG_REDIS_HOST , "localhost") ; 
//		Db.Config.put(Const.CONFIG_REDIS_PORT , "6379") ; 
//		Db.Config.put(Const.CONFIG_REDIS_PASSORD , "") ; 
//			
//		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, "http://minio.bigdata.lbxinhu.linesno.com") ;
//		Db.Config.put(Const.CONFIG_MINIO_KEY, "min_ioxinfox@9824yuhutegen") ;
//		Db.Config.put(Const.CONFIG_MINIO_SECRET , "min_ioxinfox@9824yuhutegen") ;
//		
//		Db.Config.put(Const.CONFIG_DOMAIN_HOST , "") ;
//		
//		installService.installAcp(dto);
//		
//		return AjaxResult.success() ; 
//	}
//	
//}
