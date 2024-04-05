package com.alinesno.cloud.busines.platform.install.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.lang.exception.RpcServiceRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alinesno.cloud.busines.platform.install.constants.AIP;
import com.alinesno.cloud.busines.platform.install.constants.AIPYaml;
import com.alinesno.cloud.busines.platform.install.constants.Const;
import com.alinesno.cloud.busines.platform.install.constants.InstallType;
import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallTypeDto;
import com.alinesno.cloud.busines.platform.install.service.IRunInstallService;
import com.alinesno.cloud.busines.platform.install.session.Db;
import com.alinesno.cloud.busines.platform.install.shell.domain.CmdResult;
import com.alinesno.cloud.busines.platform.install.shell.runner.CmdExecutor;
import com.alinesno.cloud.busines.platform.install.shell.runner.Log;
import com.alinesno.cloud.busines.platform.install.shell.runner.LogListener;
import com.alinesno.cloud.busines.platform.install.shell.runner.ProcListener;
import com.alinesno.cloud.busines.platform.install.utils.DatabaseUtils;
import com.alinesno.cloud.busines.platform.install.utils.NetUtils;
import com.alinesno.cloud.busines.platform.install.utils.TemplateUtils;
import com.google.common.collect.Lists;

import cn.hutool.core.thread.ThreadUtil;

/**
 * 运行安装程序
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
public class RunInstallService implements IRunInstallService {

	private static final Logger log = LoggerFactory.getLogger(PreConfigServiceImpl.class);

	@Value("${alinesno.dev.model:false}")
	private boolean devModel ; 

	@Override
	public void installAcp(InstallTypeDto dto) throws MalformedURLException, IOException {
	
		if(devModel) {
			
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
		
		}
		
		int runStatus = Db.isInstallFinish() ; 
		Assert.isTrue(runStatus == Const.PRE , "ACP服务正常安装中...") ;

		ThreadUtil.execAsync(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Db.installType = dto.getType() ;
					Db.modelType = dto.getType() ;
					String installFile = NetUtils.getInstallFile() ; 

					// 1. 下载sql脚本，处理完成写入进度表中
					processRecord(Const.STEP_1, Const.DOING);
					downloadMysqlScript(installFile) ; 
					processRecord(Const.STEP_1, Const.SUCCESS);

					// 2. 导入mysql数据库，初始化数据库表单，处理完成写入进度表中
					processRecord(Const.STEP_2, Const.DOING);
					importMysqlScript(); 
					processRecord(Const.STEP_2, Const.SUCCESS);
					
					// >>>>>>>>>>>>>>> 安装_start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

					if (InstallType.DOCKER.getType().equals(dto.getType())) { // docker install
						installAcpByDockerCompose(dto);
					} else if (InstallType.K8S.getType().equals(dto.getType())) { // k8s install 
						installAcpByKubernetes(dto);
					}
					
					// >>>>>>>>>>>>>>> 安装_end >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
					
					// 5. 验证安装是否完成
					processRecord(Const.STEP_5, Const.DOING);
					validateApplicationStatus() ; 
					processRecord(Const.STEP_5, Const.SUCCESS);
					
				} catch (Exception e) {
					// Db.initInstallProcess(); 
					throw new RpcServiceRuntimeException(e) ;
				}			
				
			}
		}) ;
		

	}
	
	/**
	 * 基于docker-compose安装
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	@Override
	public void installAcpByDockerCompose(InstallTypeDto dto) throws MalformedURLException, IOException {
		
		String installFile = NetUtils.getInstallFile() ; 

		// >>>>>>>>>>>>>>> 配置docker-compose文件_start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// 3. 下载docker yaml脚本，处理完成写入进度表中
		processRecord(Const.STEP_3, Const.DOING);
		downloadDockerYaml(installFile , dto) ;
		processRecord(Const.STEP_3, Const.SUCCESS);
		
		// 4. 执行部署命令，处理完成写入进度表中
		String dockerComposeShell = getDockerComposeShell(installFile , dto);
	
		processRecord(Const.STEP_4, Const.DOING);
		log.debug("运行安装脚本:{}" , dockerComposeShell);
		CmdExecutor executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("DOCKER_SHELL_RUNNER"), null, Lists.newArrayList(dockerComposeShell));
		CmdResult result = executor.run();
		log.debug("result = {}" , result);
	
		boolean isFinish = logListener.isFinish() ; 
		Assert.isTrue(isFinish , "安装docker-compose成功");
		processRecord(Const.STEP_4, Const.SUCCESS);
		
		// >>>>>>>>>>>>>>> 配置docker-compose文件_end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
	}

	private void downloadDockerYaml(String installFile, InstallTypeDto dto) throws IOException {
		// 下载所有脚本 
		NetUtils.downloadDockerYaml(installFile , Const.qiniuDockerDomain+  AIPYaml.docker_base_yaml.getTemplateName()) ; 
		NetUtils.downloadDockerYaml(installFile , Const.qiniuDockerDomain +  AIPYaml.docker_pro_yaml.getTemplateName()) ; 
		
		// 根据配置写入本地
		Map<String, String> config = Db.Config;
	
		TemplateUtils.writeDockerTemplate(AIPYaml.docker_base_yaml, config) ; 
		TemplateUtils.writeDockerTemplate(AIPYaml.docker_pro_yaml, config) ; 
	}

	/**
	 * 获取到docker-compose发布命令
	 * @param dto 
//	 * @param installFile
	 * @return
	 */
	private String getDockerComposeShell(String installFilePath, InstallTypeDto dto) {

		String shell = null ; 
		
		if(InstallType.BASE.getType().equals(dto.getModelType())) {
			shell = "docker-compose up -d " + (installFilePath + Const.DIR_K8S + File.separator) + AIPYaml.docker_base_yaml.getFileName() ; 
		}else if(InstallType.PRO.getType().equals(dto.getModelType())){
			shell = "docker-compose up -d " + (installFilePath + Const.DIR_K8S + File.separator) + AIPYaml.docker_pro_yaml.getFileName() ; 
		}else {
			throw new RpcServiceRuntimeException("安装类型不存在.") ; 
		}
		
		log.debug("docker-compose 安装脚本:{}" , shell);
		
		return shell;
	}

	/**
	 * 基于k8s安装
	 */
	@Override
	public void installAcpByKubernetes(InstallTypeDto dto) throws MalformedURLException, IOException {
		
		String installFile = NetUtils.getInstallFile() ; 

		// 3. 下载k8s yaml脚本，处理完成写入进度表中
		downloadK8SYaml(installFile , dto) ;

		processRecord(Const.STEP_3, Const.SUCCESS);

		// 4. 执行部署命令，处理完成写入进度表中
		String k8sApplyShell = getK8SApplayShell(installFile , dto);
		
		CmdExecutor executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(k8sApplyShell));
		CmdResult result = executor.run();
		log.debug("result = {}" , result);
	
		boolean isFinish = logListener.isFinish() ; 
		Assert.isTrue(isFinish , "安装k8s运行中");
		
		String k8sApplyIngressShell = getK8SApplayIngressShell(installFile);
		
		executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(k8sApplyIngressShell));
		result = executor.run();
		log.debug("result = {}" , result);
	
		isFinish = logListener.isFinish() ; 
		Assert.isTrue(isFinish , "安装k8sIngress运行中");
		
		processRecord(Const.STEP_4, Const.SUCCESS);
	}

	/**
	 * 下载k8s yaml文件
//	 * @param k8sProYaml
	 * @param dto
	 * @throws IOException 
	 */
	private void downloadK8SYaml(String installFile , InstallTypeDto dto) throws IOException {
		// 下载所有脚本 
		NetUtils.downloadK8SYaml(installFile , Const.qiniuK8sDomain +  AIPYaml.k8s_base_yaml.getTemplateName()) ; 
		NetUtils.downloadK8SYaml(installFile , Const.qiniuK8sDomain +  AIPYaml.k8s_base_ingress_yaml.getTemplateName()) ; 
		
		NetUtils.downloadK8SYaml(installFile , Const.qiniuK8sDomain +  AIPYaml.k8s_pro_yaml.getTemplateName()) ; 
		NetUtils.downloadK8SYaml(installFile , Const.qiniuK8sDomain +  AIPYaml.k8s_pro_ingress_yaml.getTemplateName()) ; 
		
		// 根据配置写入本地
		Map<String, String> config = Db.Config;
	
		TemplateUtils.writeK8STemplate(AIPYaml.k8s_base_yaml , config) ; 
		TemplateUtils.writeK8STemplate(AIPYaml.k8s_base_ingress_yaml , config) ; 
		
		TemplateUtils.writeK8STemplate(AIPYaml.k8s_pro_yaml , config) ; 
		TemplateUtils.writeK8STemplate(AIPYaml.k8s_pro_ingress_yaml , config) ; 
	}

	/**
	 * 下载应用全部数据库脚本
	 * @param installFile
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void downloadMysqlScript(String installFile) throws MalformedURLException, IOException {
		
		// 当前下载所有数据库执行脚本
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_initializr_admin) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_authority) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_platform) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_gateway) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.linesno_cloud_message) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_base_notice) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_base_storage) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_base_workflow) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_platform_sso) ; 
		
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_cms) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_member) ; 
		NetUtils.downloadMysqlScript(installFile ,  AIP.alinesno_cloud_shop) ; 
	}

	/**
	 * 验证安装运行的状态
	 */
	public void validateApplicationStatus() {
		
		NetUtils.isHostConnectable(AIP.alinesno_cloud_initializr_admin) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_authority) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_platform) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_gateway) ; 
		NetUtils.isHostConnectable(AIP.linesno_cloud_message) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_base_notice) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_base_storage) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_base_workflow) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_platform_sso) ; 
		
		NetUtils.isHostConnectable(AIP.alinesno_cloud_cms) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_member) ; 
		NetUtils.isHostConnectable(AIP.alinesno_cloud_shop) ; 
	}

	public String getK8SApplayIngressShell(String installFilePath) {
		String shell = "kubectl apply -f " + installFilePath + "k8s-ingress.yaml" ; 
		log.debug("k8s ingress shell = {}" , shell);
		return shell;
	}

	/**
	 * 运行shell命令
	 * @param dto 
	 * 
	 * @return
	 */
	public String getK8SApplayShell(String installFilePath, InstallTypeDto dto) {

		String shell = null ; 
		
		if(InstallType.BASE.getType().equals(dto.getModelType())) {
			shell = "kubectl apply -f " + (installFilePath + Const.DIR_K8S + File.separator) + AIPYaml.k8s_base_yaml.getFileName() ; 
		}else if(InstallType.PRO.getType().equals(dto.getModelType())){
			shell = "kubectl apply -f " + (installFilePath + Const.DIR_K8S + File.separator) + AIPYaml.k8s_pro_yaml.getFileName() ; 
		}else {
			throw new RpcServiceRuntimeException("安装类型不存在.") ; 
		}
		
		log.debug("k8s shell = {}" , shell);
		
		return shell;
	}

	/**
	 * 写入进度记录
	 * 
//	 * @param step1
//	 * @param success
	 */
	public void processRecord(String step, int status) {
		Db.InstallProcess.put(step, status);
	}

	/**
	 * 导入sql文件到数据库中
	 * 
//	 * @param sqlScriptFile
	 * @throws Exception 
	 */
	public void importMysqlScript() {

		// 创建中台数据库
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_initializr_admin);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_authority);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_platform);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_gateway);
		DatabaseUtils.createDatabase(AIP.linesno_cloud_message);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_base_notice);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_base_storage);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_base_workflow);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_platform_sso);
		
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_cms);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_member);
		DatabaseUtils.createDatabase(AIP.alinesno_cloud_shop);
		
		// 导入中台数据库
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_initializr_admin, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_authority, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_platform, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_gateway, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.linesno_cloud_message, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_base_notice, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_base_storage, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_base_workflow, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_platform_sso, NetUtils.getInstallFile());
		
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_cms, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_member, NetUtils.getInstallFile());
		DatabaseUtils.importDatabase(AIP.alinesno_cloud_shop, NetUtils.getInstallFile());
	}

	@Override
	public String getRunnerLog() {
		return null;
	}

	@Override
	public Map<String, Integer> getInstallStatus() {
		Map<String , Integer> currentMap = Db.InstallProcess;
		return currentMap ; 
	}

	private class NullProcListener implements ProcListener {

		@Override
		public void onStarted(CmdResult result) {
			System.out.println("---> onStarted ,  result = " + result.toJson());
			log.info("开始运行安装脚本:{}" , result.toJson());
		}

		@Override
		public void onLogged(CmdResult result) {
			System.out.println("---> onLogged ,  result = " + result.toJson());
			log.info(result.toJson());
		}

		@Override
		public void onExecuted(CmdResult result) {
			System.out.println("---> onExecuted ,  result = " + result.toJson());
			log.error("运行脚本异常:{}" , result.toJson());
			
			processRecord(Const.STEP_4, Const.ERROR);  // 记录进度
			
			throw new RpcServiceRuntimeException("运行脚本异常") ; 
		}

		@Override
		public void onException(CmdResult result) {
			System.out.println("---> onException ,  result = " + result.toJson());
			log.error("运行脚本异常:{}" , result.toJson());
			
			processRecord(Const.STEP_4, Const.ERROR);  // 记录进度
			
			throw new RpcServiceRuntimeException("运行脚本异常") ; 
		}
	}

	private LogListener logListener = new LogListener() {

		private boolean isFinish = false;
		private String cmdLogContent = null;

		@Override
		public void onLog(Log loger) {
			cmdLogContent = loger.getContent();
			log.info(cmdLogContent);
			isFinish = false;
		}

		@Override
		public void onFinish() {
			System.out.println("读写完成.");
			log.info("脚本运行结束.");
			isFinish = true;
		}

		public boolean isFinish() {
			return isFinish;
		}

		@Override
		public String cmdLogContent() {
			return cmdLogContent;
		}

	};

	@Override
	public int getRunnerStatus() {
		return Db.isInstallFinish() ;
	}

	@Override
	public void initInstallStatus() {
		Db.initInstallProcess(); 
	}

}
