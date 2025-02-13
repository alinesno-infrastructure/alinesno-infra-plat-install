package com.alinesno.infra.business.platform.install.constants;

import lombok.Getter;

/**
 * 应用信息
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Getter
public enum AIPYaml {

	k8s_base_yaml("k8s_base_template.yaml", "k8s_base.yaml"),
	k8s_base_ingress_yaml("k8s_ingress_base_template.yaml", "k8s_ingress_base.yaml"),

	k8s_pro_yaml("k8s_pro_template.yaml", "k8s_pro.yaml"),
	k8s_pro_ingress_yaml("k8s_ingress_pro_template.yaml", "k8s_ingress_pro.yaml"),

	docker_base_yaml("docker_compose_base_template.yaml", "docker_compose_base.yaml"),
	docker_pro_yaml("docker_compose_pro_template.yaml", "docker_compose_pro.yaml");

	private final String templateName;
	private final String fileName;
	
	AIPYaml(String templateName , String fileName) {
		this.templateName = templateName ; 
		this.fileName = fileName ; 
	}

}
