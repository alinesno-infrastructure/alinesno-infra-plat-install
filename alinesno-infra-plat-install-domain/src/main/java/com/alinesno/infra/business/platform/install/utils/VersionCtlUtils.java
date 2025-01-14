package com.alinesno.infra.business.platform.install.utils;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.business.platform.install.dto.*;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.service.impl.ParentInstall;
import com.alinesno.infra.business.platform.install.shell.domain.CmdResult;
import com.alinesno.infra.business.platform.install.shell.runner.CmdExecutor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 获取docker-compose和kubectl版本号
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
public class VersionCtlUtils {

	/**
	 * 获取到docker-compose信息
	 * 
	 * @return
	 */
	public static DockerComposeInfoDto dockerComposeInfo() {

		String infoStr = RuntimeUtil.execForStr("docker-compose -v");
		DockerComposeInfoDto info = new DockerComposeInfoDto();
		info.setInfo(infoStr);

		return info;
	}

	/**
	 * 获取到docker信息
	 * 
	 * @return
	 */
	public static DockerInfoDto dockerInfo() {

		DockerInfoDto info = new DockerInfoDto();

		try {
			String dockerInfoStr = RuntimeUtil.execForStr("/bin/sh docker info");

			Properties proper = new Properties();
			proper.load(new StringReader(dockerInfoStr));

			Enumeration<?> enum1 = proper.propertyNames();

			JSONObject json = new JSONObject();

			while (enum1.hasMoreElements()) {
				String strKey = (String) enum1.nextElement();
				String strValue = proper.getProperty(strKey);

				log.debug("key = {} , value = {}", strKey, strValue);
				json.put(strKey, strValue);
			}

			log.debug("json = {}", json.toJSONString());

			info = JSONObject.toJavaObject(json, DockerInfoDto.class);

			log.debug("info = {}", info);
		} catch (IOException e) {
			log.error("获取到Docker信息时发生错误:" + e.getMessage(), e);
			throw new RuntimeException("请确认本地是否安装Docker环境") ;
		}

        return info;
	}

	/**
	 * 获取到kubectl信息
	 * 
	 * @return
	 */
	public static KubectlInfoDto kubernetesInfo() {

		String kubectlInfo = RuntimeUtil.execForStr("kubectl version");
		kubectlInfo = kubectlInfo.substring(kubectlInfo.indexOf("{"), kubectlInfo.indexOf("}", 2) + 1);
		log.debug("kubectl info = {}", kubectlInfo);

		JSONObject json = JSONObject.parseObject(kubectlInfo);
		log.debug("json = {}", json.toJSONString());

		KubectlInfoDto info = JSONObject.toJavaObject(json, KubectlInfoDto.class);
		log.debug("info = {}", info);

		return info;
	}

	/**
	 * 确保网络是否正常
	 * 
	 * @return
	 */
	public static NetInfoDto netInfo() {
		
		boolean b = isConnect() ; 
		
		NetInfoDto netInfoDto = new NetInfoDto() ; 
		netInfoDto.setEnable(b);
		
		return netInfoDto ;
	}

	// 判断网络状态
	public static boolean isConnect() {

		boolean b = true;

		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("ping -c 4 " + "www.baidu.com");
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
				// System.out.println("返回值为:"+line);
			}
			is.close();
			isr.close();
			br.close();

			if (!sb.toString().isEmpty()) {
				String logString = "";
				if (sb.toString().indexOf("TTL") > 0) {
					// 网络畅通
					logString = "网络正常，时间 " + System.currentTimeMillis();
					log.debug("网络正常！时间为：" + logString);
				} else {
					// 网络不畅通
					logString = "网络断开，时间 " + System.currentTimeMillis();
					log.debug("网络断开！时间为" + logString);
					b = false;
				}
			}
		} catch (Exception e) {
			log.error("网络状态判断异常！" , e);
			b = false;
		}

		return b;
	}

}
