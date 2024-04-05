package com.alinesno.cloud.busines.platform.install.gateway.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 安装方式
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@ToString
@Data
public class InstallTypeDto {

	// 接收前端的安装方式选择，只设置一个接收参数
	private String type;
	
	private String modelType; // 模型模式

}
