package com.alinesno.cloud.busines.platform.install.gateway.runlog;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.cloud.busines.platform.install.gateway.dto.LoggerMessageDto;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 日志过滤
 * 
 * @author luoxiaodong
 * @since 2022年8月8日 上午6:23:43
 */
public class LogFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {

		Map<String, String> mdcMap = event.getMDCPropertyMap();
		for(String key : mdcMap.keySet()) {
			System.out.println("key = " + key + " , value = " + mdcMap.get(key));
		}

		LoggerMessageDto loggerMessage = new LoggerMessageDto(event.getFormattedMessage() , 
				DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())), event.getThreadName(),
				event.getLoggerName(), event.getLevel().levelStr);
		
		System.out.println("event = " + JSONObject.toJSONString(event));
		System.out.println("loggerMessage= " + JSONObject.toJSONString(loggerMessage));
		
		LoggerQueue.getInstance().push(loggerMessage);
		
		return FilterReply.ACCEPT;
	}

}