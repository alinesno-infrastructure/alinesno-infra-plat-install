package com.alinesno.infra.business.platform.install.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Content :日志消息实体，注意，这里为了减少篇幅，省略了get,set代码
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@ToString
@Data
public class LoggerMessageDto {

	private String body = "";
	private String timestamp  = "";
	private String threadName = "";
	private String className = "";
	private String level = "INFO";

	public LoggerMessageDto(String body, String timestamp, String threadName, String className, String level) {
		this.body = body;
		this.timestamp = timestamp;
		this.threadName = threadName;
		this.className = className;
		this.level = level;
	}

	public LoggerMessageDto(String body) {
		this.body = body;
		this.level = "INFO";
	}
}