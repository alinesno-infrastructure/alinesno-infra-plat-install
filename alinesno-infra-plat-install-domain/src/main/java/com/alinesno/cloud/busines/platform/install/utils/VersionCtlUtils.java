package com.alinesno.cloud.busines.platform.install.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerComposeInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.KubectlInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.NetInfoDto;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.RuntimeUtil;

/**
 * 获取docker-compose和kubectl版本号
 * 
 * @author luoxiaodong
 * @since 2022年8月11日 上午6:23:43
 */
public class VersionCtlUtils {

	private static final Logger log = LoggerFactory.getLogger(VersionCtlUtils.class);

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
			String dockerIfnoStr = RuntimeUtil.execForStr("/bin/sh docker info");

			Properties proper = new Properties();
			proper.load(new StringReader(dockerIfnoStr));

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
		} catch (IORuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			Process process = runtime.exec("ping " + "www.baidu.com");
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

			if (null != sb && !sb.toString().equals("")) {
				String logString = "";
				if (sb.toString().indexOf("TTL") > 0) {
					// 网络畅通
					logString = "网络正常，时间 " + System.currentTimeMillis();
					System.out.println("网络正常！时间为：" + logString);
				} else {
					// 网络不畅通
					logString = "网络断开，时间 " + System.currentTimeMillis();
					System.out.println("网络断开！时间为" + logString);
					b = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}

		return b;
	}

}
