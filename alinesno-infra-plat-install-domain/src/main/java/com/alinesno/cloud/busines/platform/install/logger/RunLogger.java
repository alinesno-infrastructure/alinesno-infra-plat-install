package com.alinesno.cloud.busines.platform.install.logger;

import java.io.File;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实时读取日志文件
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public class RunLogger {

	private static final Logger log = LoggerFactory.getLogger(RunLogger.class) ; 
	
	// 文件读取指针游标，所谓游标就是从文件的第几个字节开始读取
	public static long pointer = 0;

	/**
	 * 读取文件的方法
	 * 
	 * @author sun
	 * @date 2018年4月20日 下午9:51:46
	 * @param path
	 */
	public static void randomRed(String path) {
		try {
			File file = new File(path);
			if (file == null || file.length() < 1) {
				System.out.println("文件不存在");
				return;
			}
			@SuppressWarnings("resource")
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			// 获取RandomAccessFile对象文件指针的位置，初始位置是0
			raf.seek(pointer);// 移动文件指针位置
			String line = null;
			boolean flag = false;
			// 循环读取
			while ((line = raf.readLine()) != null) {
				flag = true;
				if (line.equals("")) {
					continue;
				}
				// 打印读取的内容,并将字节转为字符串输入，做转码处理，要不然中文会乱码
				line = new String(line.getBytes("ISO-8859-1"), "utf-8");
				System.out.println(line);
			}
			// 文件读取完毕后，将文件指针游标设置为当前指针位置 。
			// 运用这个方法可以做很多文章，比如查到自己想要的行的话，可以记下来，下次直接从这行读取
			pointer = raf.getFilePointer();
			if (flag) {
				System.err.println("当前文件游标:" + pointer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}