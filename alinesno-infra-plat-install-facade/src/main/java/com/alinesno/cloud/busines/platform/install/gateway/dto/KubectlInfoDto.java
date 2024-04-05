package com.alinesno.cloud.busines.platform.install.gateway.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * k8s基本信息
 * 
 * @author luoxiaodong
 * @since 2022年8月11日 上午6:23:43
 */
@ToString
@Data
public class KubectlInfoDto {

	private String compiler;
	private String major;
	private String gitVersion;
	private String gitCommit;
	private String platform;
	private String buildDate;
	private String goVersion;
	private String minor;
	private String gitTreeState;

}
