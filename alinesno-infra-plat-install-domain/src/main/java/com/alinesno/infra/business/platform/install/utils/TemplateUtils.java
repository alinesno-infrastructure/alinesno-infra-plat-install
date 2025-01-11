package com.alinesno.infra.business.platform.install.utils;

import com.alinesno.infra.business.platform.install.constants.AIPYaml;
import com.alinesno.infra.business.platform.install.dto.InstallForm;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.Map;

/**
 * 模板工具类
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public class TemplateUtils {

//	public static VelocityEngine k8sEngine;
//	public static VelocityEngine dockerEngine;
//
//	static {
//		String k8sTemplatePath = NetUtils.getInstallFile() ;
//		k8sEngine = new VelocityEngine();
//		k8sEngine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, k8sTemplatePath);
//		k8sEngine.setProperty(Velocity.RUNTIME_LOG, NetUtils.getInstallFile() + File.separator + "velocity.log");
//		k8sEngine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
//		k8sEngine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
//		k8sEngine.init();
//
//		String dockerTemplatePath = NetUtils.getInstallFile() ;
//		dockerEngine = new VelocityEngine();
//		dockerEngine.setProperty(Velocity.RUNTIME_LOG, NetUtils.getInstallFile() + File.separator + "velocity.log");
//		dockerEngine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, dockerTemplatePath);
//		dockerEngine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
//		dockerEngine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
//		dockerEngine.init();
//	}

//	/**
//	 * 写入模板工具类
//	 *
//	 * @param yaml
//	 * @param config
//	 * @throws IOException
//	 */
//	public static void writeK8STemplate(AIPYaml yaml, Map<String, String> config) throws IOException {
//
//		String writePath = NetUtils.getInstallFile() + File.separator +  yaml.getFileName();
//		Template template = k8sEngine.getTemplate(yaml.getTemplateName()) ;
//
//		VelocityContext root = new VelocityContext();
//
//		for(String key : config.keySet()) {
//			root.put(key , config.get(key));
//		}
//
//		File writeFile = new File(writePath) ;
//
//		if(writeFile.exists()) {
//			FileUtils.forceDelete(writeFile);
//		}
//
//		Writer mywriter = new PrintWriter(new FileOutputStream(writeFile));
//		template.merge(root, mywriter);
//		mywriter.flush();
//
//	}
//
//	public static void writeDockerTemplate(AIPYaml yaml, Map<String, String> config) throws IOException {
//
//		String writePath = NetUtils.getInstallFile() + File.separator  +  yaml.getFileName();
//		Template template = dockerEngine.getTemplate(yaml.getTemplateName()) ;
//
//		VelocityContext root = new VelocityContext();
//
//		for(String key : config.keySet()) {
//			root.put(key , config.get(key));
//		}
//
//		File writeFile = new File(writePath) ;
//
//		if(writeFile.exists()) {
//			FileUtils.forceDelete(writeFile);
//		}
//
//		Writer mywriter = new PrintWriter(new FileOutputStream(writeFile));
//		template.merge(root, mywriter);
//		mywriter.flush();
//
//	}

	public static void renderTemplate(String envK8SYaml, InstallForm installForm) {

		// 替换环境变量

	}
}
