package com.alinesno.infra.business.platform.install.dto;

import lombok.Data;
import lombok.ToString;

/**
 * Docker基本信息
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Data
@ToString
public class DockerInfoDto {
	
	private String server;
	private String stopped;
	private String seccomp;
	private String containers;
	private String security;
	private String runc;
	private String profile;
	private String name;
	private String swarm;
	private String init;
	private String supports;
	private String number;
	private String docker;
	private String network;
	private String runtimes;
	private String registries;
	private String ID;
	private String kernel;
	private String debug;
	private String paused;
	private String plugins;
	private String logging;
	private String insecure;
	private String architecture;
	private String experimental;
	private String images;
	private String live;
	private String cgroup;
	private String containerd;
	private String registry;
	private String operating;
	private String WARNING;
	private String storage;
	private String CPUs;
	private String volume;
	private String oSType;
	private String total;
	private String running;
	private String backing;

}
