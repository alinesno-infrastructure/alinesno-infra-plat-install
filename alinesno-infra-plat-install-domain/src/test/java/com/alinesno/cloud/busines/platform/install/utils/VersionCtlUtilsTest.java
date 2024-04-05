package com.alinesno.cloud.busines.platform.install.utils;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerComposeInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.KubectlInfoDto;

/**
 * 获取信息版本号工具类
 * 
 * @author luoxiaodong
 * @since 2022年8月11日 上午6:23:43
 */
class VersionCtlUtilsTest {

	private static final Logger log = LoggerFactory.getLogger(VersionCtlUtilsTest.class);

	@Test
	void testDockerComposeInfo() {
		
		DockerComposeInfoDto dockerComposeInfoDto = VersionCtlUtils.dockerComposeInfo() ; 
		log.debug("DockerComposeInfoDto = {}" , dockerComposeInfoDto);
		
	}

	@Test
	void testDockerInfo() throws IOException, InterruptedException {
		
		DockerInfoDto dockerInfoDto  = VersionCtlUtils.dockerInfo() ; 
		log.debug("dockerInfoDto = {}" , dockerInfoDto);
		
	}
	
	@Test
	void testDockerInfoStr() throws IOException {
		
		String dockerIfnoStr = "Containers: 3\n"
				+ " Running: 3\n"
				+ " Paused: 0\n"
				+ " Stopped: 0\n"
				+ "Images: 77\n"
				+ "Server Version: 1.13.1\n"
				+ "Storage Driver: overlay2\n"
				+ " Backing Filesystem: extfs\n"
				+ " Supports d_type: true\n"
				+ " Native Overlay Diff: true\n"
				+ "Logging Driver: journald\n"
				+ "Cgroup Driver: systemd\n"
				+ "Plugins:\n"
				+ " Volume: local\n"
				+ " Network: bridge host macvlan null overlay\n"
				+ "Swarm: inactive\n"
				+ "Runtimes: docker-runc runc\n"
				+ "Default Runtime: docker-runc\n"
				+ "Init Binary: /usr/libexec/docker/docker-init-current\n"
				+ "containerd version:  (expected: aa8187dbd3b7ad67d8e5e3a15115d3eef43a7ed1)\n"
				+ "runc version: 66aedde759f33c190954815fb765eedc1d782dd9 (expected: 9df8b306d01f59d3a8029be411de015b7304dd8f)\n"
				+ "init version: fec3683b971d9c3ef73f284f176672c44b448662 (expected: 949e6facb77383876aeff8a6944dde66b3089574)\n"
				+ "Security Options:\n"
				+ " seccomp\n"
				+ "  WARNING: You're not using the default seccomp profile\n"
				+ "  Profile: /etc/docker/seccomp.json\n"
				+ "Kernel Version: 3.10.0-1160.21.1.el7.x86_64\n"
				+ "Operating System: CentOS Linux 7 (Core)\n"
				+ "OSType: linux\n"
				+ "Architecture: x86_64\n"
				+ "Number of Docker Hooks: 3\n"
				+ "CPUs: 2\n"
				+ "Total Memory: 3.7 GiB\n"
				+ "Name: izm5e9pfu150pegnyjwluaz\n"
				+ "ID: 73D6:OKLE:VWH5:ZJYF:A3LF:DSQ7:Y6OH:6PJX:AZZJ:AJCH:VWUC:HTAZ\n"
				+ "Docker Root Dir: /var/lib/docker\n"
				+ "Debug Mode (client): false\n"
				+ "Debug Mode (server): false\n"
				+ "Registry: https://index.docker.io/v1/\n"
				+ "Experimental: false\n"
				+ "Insecure Registries:\n"
				+ " 127.0.0.0/8\n"
				+ "Live Restore Enabled: false\n"
				+ "Registries: docker.io (secure)" ; 
		
		Properties proper = new Properties();
		proper.load(new StringReader(dockerIfnoStr));
	
		Enumeration<?> enum1 = proper.propertyNames();
	
		JSONObject json = new JSONObject() ; 
		
		while (enum1.hasMoreElements()) {
		    String strKey = (String) enum1.nextElement();
		    String strValue = proper.getProperty(strKey);
		    
		    log.debug("key = {} , value = {}" , strKey , strValue);
		    json.put(strKey, strValue) ; 
		}
	
		log.debug("json = {}" , json.toJSONString());
		
		DockerInfoDto info = JSONObject.toJavaObject(json, DockerInfoDto.class) ; 
		log.debug("info = {}" , info);
	}

	@Test
	void testKubernetesInfo() {
		fail("Not yet implemented");
	}

	@Test
	void testKubernetesInfoStr() {

		String kubectlInfo = "Client Version: version.Info{Major:\"1\", Minor:\"17\", GitVersion:\"v1.17.0\", GitCommit:\"70132b0f130acc0bed193d9ba59dd186f0e634cf\", GitTreeState:\"clean\", BuildDate:\"2019-12-07T21:20:10Z\", GoVersion:\"go1.13.4\", Compiler:\"gc\", Platform:\"linux/amd64\"}";
		kubectlInfo = kubectlInfo.substring(kubectlInfo.indexOf("{"), kubectlInfo.indexOf("}", 2) + 1) ; 
		log.debug("kubectl info = {}" , kubectlInfo);

		JSONObject json = JSONObject.parseObject(kubectlInfo);
		log.debug("json = {}" , json.toJSONString());
		
		KubectlInfoDto info = JSONObject.toJavaObject(json, KubectlInfoDto.class) ; 
		log.debug("info = {}" , info);

	}

}
