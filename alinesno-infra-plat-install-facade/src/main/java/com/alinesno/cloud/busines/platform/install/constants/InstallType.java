package com.alinesno.cloud.busines.platform.install.constants;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 安装类型
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Getter
public enum InstallType {

	BASE("base") , 
	PRO("pro") ,
	
	DOCKER("docker"), 
	K8S("k8s");

	private String type;
	private List<String> typeList = new ArrayList<String>();

	InstallType(String type) {
		this.type = type;
	}

	public List<String> getTypeList() {

		typeList.add(DOCKER.type);
		typeList.add(K8S.type);

		return typeList;
	}

}
