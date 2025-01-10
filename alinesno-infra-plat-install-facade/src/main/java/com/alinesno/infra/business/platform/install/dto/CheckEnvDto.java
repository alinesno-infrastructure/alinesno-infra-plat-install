package com.alinesno.infra.business.platform.install.dto;

import com.alinesno.infra.common.core.monitor.Server;
import lombok.Data;
import lombok.ToString;

/**
 * 检查信息实体对象
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@ToString
@Data
public class CheckEnvDto {

	/**
	 * 基础环境
	 */
	private Server server;

	/**
	 * docker镜像信息
	 */
	private DockerInfoDto dockerInfo;

	/**
	 * docker-compose信息
	 */
	private DockerComposeInfoDto dockerComposeInfo;

	/**
	 * k8s信息
	 */
	private KubectlInfoDto kubernetesInfo;

	/**
	 * 网络连接信息
	 */
	private  NetInfoDto netInfo ;

}
