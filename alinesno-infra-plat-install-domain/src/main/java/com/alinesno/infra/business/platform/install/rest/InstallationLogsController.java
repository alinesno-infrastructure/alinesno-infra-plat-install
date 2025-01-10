package com.alinesno.infra.business.platform.install.rest;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.business.platform.install.dto.LoggerMessageDto;
import com.alinesno.infra.business.platform.install.service.ISSEService;
import com.alinesno.infra.common.facade.response.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.lang.exception.RpcServiceRuntimeException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class InstallationLogsController {

    @Autowired
    private ISSEService service;

    /**
     * 建立SSE连接
     * @param clientId
     * @return
     */
    @GetMapping(value = "openConn/{clientId}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@PathVariable("clientId") String clientId) {

        final SseEmitter emitter = service.getConn(clientId);
        CompletableFuture.runAsync(() -> {
            try {
                service.send(clientId , JSONObject.toJSONString(new LoggerMessageDto("hello world,connection successful !!")));
            } catch (Exception e) {
                throw new RpcServiceRuntimeException("推送数据异常");
            }
        });

        return emitter;
    }

    /**
     * 关闭SSE连接
     * @param clientId
     * @return
     */
    @GetMapping("closeConn/{clientId}")
    public AjaxResult closeConn(@PathVariable("clientId") String clientId) {
        service.closeConn(clientId);
        return AjaxResult.success("连接已关闭");
    }

}