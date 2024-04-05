package com.alinesno.cloud.busines.platform.install.gateway.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Content :日志消息实体，注意，这里为了减少篇幅，省略了get,set代码
 * 
 * @author luoxiaodong
 * @since 2022年8月8日 上午6:23:43
 */
@ToString
@Data
public class LoggerMessageDto {

	private String body;
	private String timestamp;
	private String threadName;
	private String className;
	private String level;

	public LoggerMessageDto(String body, String timestamp, String threadName, String className, String level) {
		this.body = body;
		this.timestamp = timestamp;
		this.threadName = threadName;
		this.className = className;
		this.level = level;
	}

}