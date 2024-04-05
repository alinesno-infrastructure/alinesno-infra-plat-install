package com.alinesno.cloud.busines.platform.install;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import com.alinesno.infra.common.web.adapter.enable.EnableApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alinesno.cloud.busines.platform.install.gateway.dto.LoggerMessageDto;
import com.alinesno.cloud.busines.platform.install.gateway.runlog.LoggerQueue;

/**
 * 启动入口
 *
 * @author ${author} ${authorEmail}
 * @since 2020-12-08 10:12:366
 */
@EnableApi
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // 不需要加载数据库时
@EnableScheduling
public class InstallApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstallApplication.class, args);
	}
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
    
	/**
	 * 推送日志到/topic/pullLogger
	 */
	@PostConstruct
	public void pushLogger(){
		ExecutorService executorService=Executors.newFixedThreadPool(2);
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						LoggerMessageDto log = LoggerQueue.getInstance().poll();
						if(log!=null){
							if(messagingTemplate!=null){
								messagingTemplate.convertAndSend("/topic/pullLogger",log);
							}
						}
					} catch (Exception e) {
						log.error("消息异常:{}" , e.getMessage());
					}
				}
			}
		};
		executorService.submit(runnable);
		executorService.submit(runnable);
	}

}
