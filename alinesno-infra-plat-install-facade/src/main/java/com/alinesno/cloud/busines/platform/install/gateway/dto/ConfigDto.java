package com.alinesno.cloud.busines.platform.install.gateway.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.NotNull;

/**
 * 中间件配置实体
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Data
@ToString
public class ConfigDto {

	@NotNull(message = "数据库地址不能为空")
	private String databaseUrl; // 数据库地址

	@NotNull(message = "数据库账号不能为空")
	private String databaseUser; // 数据库账号

	@NotNull(message = "数据库密码不能为空")
	private String databasePwd; // 数据库密码

	@NotNull(message = " minio保存地址不能为空")
	private String minioUrl; // minio保存地址

	@NotNull(message = " minio账号名不能为空")
	private String minioUser; // 账号名

	@NotNull(message = "minio密码不能为空")
	private String minioPwd; // 密码

	@URL(message ="域名地址不规范")
	@URL(regexp = "^(http|https)://([\\w.]+\\/?)\\S*")
	@NotNull(message = "域名地址不能为空")
	private String domainHost; // 域名地址

}
