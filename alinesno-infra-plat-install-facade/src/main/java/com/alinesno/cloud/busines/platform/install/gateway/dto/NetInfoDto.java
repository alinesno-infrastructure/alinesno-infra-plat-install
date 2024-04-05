package com.alinesno.cloud.busines.platform.install.gateway.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 网络连接信息
 * 
 * @author luoxiaodong
 * @since 2022年8月13日 上午6:23:43
 */
@ToString
@Data
public class NetInfoDto {

	private boolean enable;
	private String connect;

}
