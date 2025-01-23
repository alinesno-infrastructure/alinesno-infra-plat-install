package com.alinesno.infra.business.platform.install.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.*;

/**
 * 文件下载工具
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Slf4j
public class NetUtils {

	/**
	 * 验证是否正常启动
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean isHostConnectable(String host, int port) {
		Socket socket = new Socket();
	
		int socketTImeout = 45 ; // 秒
		
		try {
			socket.connect(new InetSocketAddress(host, port) , socketTImeout * 1000);
		} catch (IOException e) {
			log.error("验证连接{}:{}失败" , host , port);
			return false;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

//	/**
//	 * 下载sql脚本
//	 *
//	 * @throws IOException
//	 * @throws MalformedURLException
//	 */
//	public static void downloadMysqlScript(String installPath, AIP acp)
//			throws MalformedURLException, IOException {
//
//		log.debug("下载服务[{}]数据库" , acp.getAppName());
//
//		String downloadUrl = Const.qiniuSqlDomain +  acp.getSqlFilename() ;
//
//		File installFile = new File(installPath + File.separator + Const.DIR_SQL);
//
//		if (!installFile.exists()) {
//			FileUtils.forceMkdir(installFile) ;
//		}
//
//		String savePath = installFile.getAbsolutePath() + File.separator  + NetUtils.filename(downloadUrl);
//		if (!new File(savePath).exists()) { // 存在则不再下载
//			NetUtils.download(downloadUrl, savePath);
//		}
//
//	}

	/**
	 * 下载文件到指定目录
	 * 
	 * @param dowUrl:http地址
	 * @param dowPath:指定目录
	 */
	public static String download(String dowUrl, String dowPath) {

		try {

			log.info("下载地址是：" + dowUrl + ",存储地址是：" + dowPath);
			BufferedInputStream bin = getBufferedInputStream(dowUrl);

			File filed = new File(dowPath);

			File parentFile = filed.getParentFile() ;
			if(!parentFile.exists()){
				FileUtils.forceMkdir(parentFile) ;
			}

			OutputStream out = new FileOutputStream(filed);
			int size = 0;

			byte[] b = new byte[2048];
			// 把输入流的文件读取到字节数据b中，然后输出到指定目录的文件
			while ((size = bin.read(b)) != -1) {

				out.write(b, 0, size);
			}
			// 关闭资源
			bin.close();
			out.close();

			return "200";

		} catch (MalformedURLException e) {
			log.error("下载地址有误：" + e.getMessage());
			return "500";
		} catch (IOException e) {
			e.printStackTrace();
			log.error("下载文件时发生错误：" + e.getMessage());
			return "400";
		}
	}

	@NotNull
	private static BufferedInputStream getBufferedInputStream(String dowUrl) throws IOException {
		URL url = new URL(dowUrl);

		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;// http的连接类

		// String contentType = httpURLConnection.getContentType();//请求类型,可用来过滤请求，

		httpURLConnection.setConnectTimeout(1000 * 5);// 设置超时
		// httpURLConnection.setRequestMethod("POST");// 设置请求方式，默认是GET
		httpURLConnection.setRequestProperty("Charset", "UTF-8");// 设置字符编码

		httpURLConnection.connect();// 打开连接

		BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
		return bin;
	}

	/**
	 * 根据url获取文件名称
	 * 
	 * @return
	 */
	public static String filename(String url) {

		int lastIndex = url.lastIndexOf("/");
		String filename = url.substring(lastIndex + 1, url.length());

		log.debug("url = {} , filename = {}", url, filename);

		return filename;

	}

//	public static void isHostConnectable(AIP acp) {
//		log.debug("验证【{}】状态" , acp.getAppName());
//		boolean b = isHostConnectable(acp.getHost(), acp.getPort());
//		log.debug("验证【{}】安装:{}" , acp.getAppName() , b?"成功":"失败");
//	}

//	/**
//	 * 下载yaml文件下载
//	 *
//	 * @throws IOException
//	 */
//	public static void downloadK8SYaml(String installPath, String downloadUrl) throws IOException {
//		File installFile = new File(installPath + File.separator + Const.DIR_K8S) ;
//
//		if (!installFile.exists()) {
//			FileUtils.forceMkdir(installFile);
//		}
//
//		String savePath =  installFile.getAbsoluteFile() + File.separator + NetUtils.filename(downloadUrl);
//		if (!new File(savePath).exists()) { // 存在则不再下载
//			NetUtils.download(downloadUrl, savePath);
//		}
//
//	}
	

	/**
	 * 获取到安装目录
	 * 
	 * @return
	 */
	@SneakyThrows
	public static String getInstallFile() {
		String userHomeDir = System.getProperty("user.home");
		String installPath = userHomeDir + File.separator + ".aip-install";
		String installDataPath = userHomeDir + File.separator + ".aip-install-data";

		if (!new File(installPath).exists()) {
			FileUtils.forceMkdir(new File(installPath));
		}

		if (!new File(installDataPath).exists()) {
			FileUtils.forceMkdir(new File(installDataPath));
		}

		log.info("AIP安装目录:{}，数据目录:{}" , installPath , installDataPath);

		return installPath;
	}

	public static String getInstallDataFile() {
		String userHomeDir = System.getProperty("user.home");
        return userHomeDir + File.separator + ".aip-install-data";
	}

//	public static void downloadDockerYaml(String installPath, String downloadUrl) throws IOException {
//
//		File installFile = new File(installPath + File.separator + Const.DIR_DOCKER) ;
//
//		if (!installFile.exists()) {
//			FileUtils.forceMkdir(installFile);
//		}
//
//		String savePath =  installFile.getAbsoluteFile() + File.separator + NetUtils.filename(downloadUrl);
//		if (!new File(savePath).exists()) { // 存在则不再下载
//			NetUtils.download(downloadUrl, savePath);
//		}
//
//	}

}
