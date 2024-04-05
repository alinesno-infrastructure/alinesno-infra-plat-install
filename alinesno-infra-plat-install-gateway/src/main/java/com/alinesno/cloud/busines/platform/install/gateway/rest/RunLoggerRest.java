package com.alinesno.cloud.busines.platform.install.gateway.rest;

import java.io.IOException;
import java.io.InputStream;

//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.web.bind.annotation.RestController;

import com.alinesno.cloud.busines.platform.install.gateway.utils.TailfLogThread;

/**
 * WebSocket日志输出
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@ServerEndpoint("/log")
@RestController
public class RunLoggerRest {

	private Process process;
	private InputStream inputStream;

	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(Session session) {
		try {
			process = Runtime.getRuntime().exec("tail -f /logs/es-sync/es-sync.log");
			inputStream = process.getInputStream();
			TailfLogThread thread = new TailfLogThread(inputStream, session);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (process != null)
			process.destroy();
	}

	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}
}