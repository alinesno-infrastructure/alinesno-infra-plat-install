package com.alinesno.cloud.busines.platform.install.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alinesno.cloud.busines.platform.install.constants.AIP;
import com.alinesno.cloud.busines.platform.install.constants.Const;

/**
 * 文件下载工具
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public class NetUtils {

	private static final Logger log = LoggerFactory.getLogger(NetUtils.class);

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

	/**
	 * 下载sql脚本
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void downloadMysqlScript(String installPath, AIP acp)
			throws MalformedURLException, IOException {
		
		log.debug("下载服务[{}]数据库" , acp.getAppName());
		
		String downloadUrl = Const.qiniuSqlDomain +  acp.getSqlFilename() ; 

		File installFile = new File(installPath + File.separator + Const.DIR_SQL);

		if (!installFile.exists()) {
			FileUtils.forceMkdir(installFile) ; 
		}

		String savePath = installFile.getAbsolutePath() + File.separator  + NetUtils.filename(downloadUrl);
		if (!new File(savePath).exists()) { // 存在则不再下载
			NetUtils.download(downloadUrl, savePath);
		}

	}

	/**
	 * 下载文件到指定目录
	 * 
	 * @param dowUrl:http地址
	 * @param dowPath:指定目录
	 */
	public static String download(String dowUrl, String dowPath) {

		try {

			log.info("下载地址是：" + dowUrl + ",存储地址是：" + dowPath);
			URL url = new URL(dowUrl);

			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;// http的连接类

			// String contentType = httpURLConnection.getContentType();//请求类型,可用来过滤请求，

			httpURLConnection.setConnectTimeout(1000 * 5);// 设置超时
			// httpURLConnection.setRequestMethod("POST");// 设置请求方式，默认是GET
			httpURLConnection.setRequestProperty("Charset", "UTF-8");// 设置字符编码

			httpURLConnection.connect();// 打开连接

			BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

			String path = dowPath;// 指定存放位置
			File filed = new File(path);

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

			e.printStackTrace();
			return "500";

		} catch (IOException e) {

			e.printStackTrace();
			return "400";
		}
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

	public static void isHostConnectable(AIP acp) {
		log.debug("验证【{}】状态" , acp.getAppName());
		boolean b = isHostConnectable(acp.getHost(), acp.getPort());
		log.debug("验证【{}】安装:{}" , acp.getAppName() , b?"成功":"失败");
	}

	/**
	 * 下载yaml文件下载
	 * 
	 * @param installFile
	 * @param string
	 * @throws IOException 
	 */
	public static void downloadK8SYaml(String installPath, String downloadUrl) throws IOException {
		File installFile = new File(installPath + File.separator + Const.DIR_K8S) ; 

		if (!installFile.exists()) {
			FileUtils.forceMkdir(installFile);
		}

		String savePath =  installFile.getAbsoluteFile() + File.separator + NetUtils.filename(downloadUrl);
		if (!new File(savePath).exists()) { // 存在则不再下载
			NetUtils.download(downloadUrl, savePath);
		}

	}
	

	/**
	 * 获取到安装目录
	 * 
	 * @return
	 */
	public static String getInstallFile() {
		String userHomeDir = System.getProperty("user.home");
		String installPath = userHomeDir + File.separator + ".acp-install";
		
		log.info("ACP安装目录:{}" , installPath);

		return installPath;
	}

	public static void downloadDockerYaml(String installPath, String downloadUrl) throws IOException {
		
		File installFile = new File(installPath + File.separator + Const.DIR_DOCKER) ; 

		if (!installFile.exists()) {
			FileUtils.forceMkdir(installFile);
		}

		String savePath =  installFile.getAbsoluteFile() + File.separator + NetUtils.filename(downloadUrl);
		if (!new File(savePath).exists()) { // 存在则不再下载
			NetUtils.download(downloadUrl, savePath);
		}
		
	}


}
