package com.alinesno.cloud.rest.gateway.adapter.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author luoxiaodong
 * @version 1.0.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class CorsFilter implements Filter {
	
	private final List<String> allowedOrigins = List.of("http://localhost:1024");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		
		// 允许所有的域访问，可以设置只允许自己的域访问
		String origin = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "*");
		// 允许所有方式的请求
		response.setHeader("Access-Control-Allow-Methods", "*") ;
		// 头信息缓存有效时长（如果不设 Chromium 同时规定了一个默认值 5 秒），没有缓存将已OPTIONS进行预请求
		response.setHeader("Access-Control-Max-Age", "3600");
		// 允许的头信息
		response.setHeader("Access-Control-Allow-Headers", "*");
		// 认证信息
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// 处理x-json
		response.setHeader("Access-Control-Expose-Headers", "Content-Length, X-JSON");

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {

	}
}
