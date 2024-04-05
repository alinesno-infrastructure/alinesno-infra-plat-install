package com.alinesno.cloud.busines.platform.install.gateway.runlog;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alinesno.cloud.busines.platform.install.gateway.dto.LoggerMessageDto;

/**
 * 创建阻塞队列，日志临时载体
 * 
 * @author luoxiaodong
 * @since 2022年8月8日 上午6:23:43
 */
public class LoggerQueue {
	
	// 队列大小
	public static final int QUEUE_MAX_SIZE = 100000 * 100;
	
	private static LoggerQueue alarmMessageQueue = new LoggerQueue();
	
	// 阻塞队列
	private BlockingQueue<LoggerMessageDto> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

	private LoggerQueue() {
	}

	public static LoggerQueue getInstance() {
		return alarmMessageQueue;
	}

	/**
	 * 消息入队
	 * 
	 * @param log
	 * @return
	 */
	public boolean push(LoggerMessageDto log) {
		return this.blockingQueue.add(log);// 队列满了就抛出异常，不阻塞
	}

	/**
	 * 消息出队
	 * 
	 * @return
	 */
	public LoggerMessageDto poll() {
		LoggerMessageDto result = null;
		try {
			result = this.blockingQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
}