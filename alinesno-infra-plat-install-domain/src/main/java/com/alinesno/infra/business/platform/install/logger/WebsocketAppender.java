package com.alinesno.infra.business.platform.install.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.business.platform.install.constants.Const;
import com.alinesno.infra.business.platform.install.dto.LoggerMessageDto;
import com.alinesno.infra.business.platform.install.service.ISSEService;
import lombok.SneakyThrows;

import java.text.DateFormat;
import java.util.Date;

/**
 * 日志输出到列队中
 * @author luoxiaodong
 * @since 2022年8月9日 上午6:23:43
 */
public class WebsocketAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    @SneakyThrows
    @Override
    protected void append(ILoggingEvent event) {

        LoggerMessageDto loggerMessage = new LoggerMessageDto(
                event.getFormattedMessage(),
                DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                event.getThreadName(),
                event.getLoggerName(),
                event.getLevel().levelStr
        );

        ISSEService sseService = SpringUtil.getBean(ISSEService.class);
        sseService.send(Const.DEFAULT_SSE_CHANNEL , JSONObject.toJSONString(loggerMessage));
    }
}