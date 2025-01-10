package com.alinesno.infra.business.platform.install.dto;

import lombok.Data;
import lombok.ToString;

/**
 * k8s基本信息
 * 
 * @author luoxiaodong
 * @version 1.0.0
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
