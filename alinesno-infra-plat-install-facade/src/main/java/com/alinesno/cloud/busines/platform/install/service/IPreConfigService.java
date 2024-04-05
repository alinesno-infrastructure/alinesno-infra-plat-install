package com.alinesno.cloud.busines.platform.install.service;

import java.util.Map;

import com.alinesno.cloud.busines.platform.install.gateway.dto.CheckEnvDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.ConfigDto;

/**
 * 配置服务账号密码
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public interface IPreConfigService {

	/**
	 * 验证配置是否正确
	 * 
	 * @param configDto
	 */
	public void checkConfig(ConfigDto configDto);

	/**
	 * 获取当前配置
	 * 
	 * @return
	 */
	public Map<String, String> getCurrentConfig();

	/**
	 * 检查系统配置信息
	 * 
	 * @param checkEnvDto
	 */
	public CheckEnvDto checkEnvInfo(CheckEnvDto checkEnvDto);

}
