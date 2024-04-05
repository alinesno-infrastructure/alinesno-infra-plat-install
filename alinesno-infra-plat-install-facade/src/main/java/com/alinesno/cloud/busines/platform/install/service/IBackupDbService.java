package com.alinesno.cloud.busines.platform.install.service;

/**
 * ACP数据备份服务，用于数据备份和升级
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
public interface IBackupDbService {

	/**
	 * 数据备份
	 */
	public void backup() ; 

	/**
	 * 数据恢复
	 */
	public void restore(String date) ; 
	
}
