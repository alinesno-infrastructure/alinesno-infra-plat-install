package com.alinesno.cloud.busines.platform.install.gateway.dto;

/**
 * 安装数据项实体
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public class ModelItemDto {

	private String type;
	private String baseItem;
	private String proItem;
	private String companyItem;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBaseItem() {
		return baseItem;
	}

	public void setBaseItem(String baseItem) {
		this.baseItem = baseItem;
	}

	public String getProItem() {
		return proItem;
	}

	public void setProItem(String proItem) {
		this.proItem = proItem;
	}

	public String getCompanyItem() {
		return companyItem;
	}

	public void setCompanyItem(String companyItem) {
		this.companyItem = companyItem;
	}

}
