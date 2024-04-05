package com.alinesno.cloud.busines.platform.install.gateway.dto;

/**
 * 安装的实体对象
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public class InstallItemDto {

	private String label; // 进度标识
	private String name; // 进度名称
	private int status; // 进度状态

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
