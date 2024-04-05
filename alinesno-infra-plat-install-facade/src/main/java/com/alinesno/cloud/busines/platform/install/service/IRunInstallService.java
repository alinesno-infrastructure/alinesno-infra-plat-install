package com.alinesno.cloud.busines.platform.install.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallTypeDto;

/**
 * 安装服务
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public interface IRunInstallService {

	/**
	 * 安装ACP服务
	 * 
	 * @param dto
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void installAcp(InstallTypeDto dto) throws MalformedURLException, IOException;

	/**
	 * 安装ACP服务 By Kuberneates
	 * 
	 * @param dto
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void installAcpByKubernetes(InstallTypeDto dto) throws MalformedURLException, IOException;

	/**
	 * 安装ACP服务 By DockerCompose
	 * 
	 * @param dto
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	void installAcpByDockerCompose(InstallTypeDto dto) throws MalformedURLException, IOException;

	/**
	 * 获取运行日志
	 * 
	 * @return
	 */
	String getRunnerLog();

	/**
	 * 获取安装状态
	 * 
	 * @return
	 */
	Map<String, Integer> getInstallStatus();

	/**
	 * 获取运行状态
	 * 
	 * @return
	 */
	int getRunnerStatus();

	/**
	 * 初始化运行状态
	 */
	void initInstallStatus();
}
