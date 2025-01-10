package com.alinesno.infra.business.platform.install.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 网络连接信息
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@ToString
@Data
public class NetInfoDto {

	private boolean enable;
	private String connect;

}
