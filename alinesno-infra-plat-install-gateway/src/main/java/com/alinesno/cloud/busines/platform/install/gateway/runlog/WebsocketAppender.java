package com.alinesno.cloud.busines.platform.install.gateway.runlog;
 
import java.text.DateFormat;
import java.util.Date;

import com.alinesno.cloud.busines.platform.install.gateway.dto.LoggerMessageDto;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * 日志输出到列队中
 * @author luoxiaodong
 * @since 2022年8月9日 上午6:23:43
 */
public class WebsocketAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
 
    @Override
    protected void append(ILoggingEvent event) {
        try {
            LoggerMessageDto loggerMessage = new LoggerMessageDto(
                    event.getFormattedMessage(),
                    DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                    event.getThreadName(),
                    event.getLoggerName(),
                    event.getLevel().levelStr
            );
            LoggerQueue.getInstance().push(loggerMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
}