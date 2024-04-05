package com.alinesno.cloud.busines.platform.install.gateway.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alinesno.infra.common.facade.response.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alinesno.cloud.busines.platform.install.constants.Const;
import com.alinesno.cloud.busines.platform.install.constants.InstallType;
import com.alinesno.cloud.busines.platform.install.constants.Const.stepText;
import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallItemDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.InstallTypeDto;
import com.alinesno.cloud.busines.platform.install.service.IRunInstallService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 安装检查实体
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Api(tags = "安装运行")
@RestController
@RequestMapping("/api/install/run")
public class RunInstallRest {
	private  static String modeltype;
	private  static String Installtype;
	private static final Logger log = LoggerFactory.getLogger(RunInstallRest.class);

	@Autowired
	private IRunInstallService installService;

	/**
	 * 保存前端安装模型
	 * @return
	 */
	@ApiOperation(value = "保存前端安装方式")
	@PostMapping("/modeltype")
	public AjaxResult modeltype(String type) {
		modeltype = modeltype;
		return AjaxResult.success();
	}

	/**
	 * 保存前端安装方式
	 * @return
	 */
	@ApiOperation(value = "保存前端安装方式")
	@PostMapping("/Installtype")
	public AjaxResult Installtype(String type) {
		Installtype = Installtype;
		return AjaxResult.success();
	}



	/**
	 * 安装状态轮询
	 * @return
	 */
	@ApiOperation(value = "安装状态轮询")
	@GetMapping("/runStatus")
	public AjaxResult runStatus() {

		Map<String , Integer> installStatus = installService.getInstallStatus() ; 
		
		return AjaxResult.success(installStatus);
	}

	/**
	 * 安装日志获取输出
	 * 
	 * @param dto
	 * @return
	 */
	@ApiOperation(value = "安装日志获取输出")
	@GetMapping("/log")
	public AjaxResult log(InstallTypeDto dto) {
		
		String json = installService.getRunnerLog() ; 

		return AjaxResult.success(json);
	}
	
	/**
	 * 获取安装的状态
	 * @return
	 */
	@ApiOperation(value = "获取安装的状态")
	@GetMapping("/installStatus")
	public AjaxResult installStatus() {
		
		int runStatus = installService.getRunnerStatus() ; 

		return AjaxResult.success(runStatus);
	}

	/**
	 * 重新运行安装
	 * @return
	 */
	@ApiOperation(value = "重新运行安装")
	@GetMapping("/reInstall")
	public AjaxResult reInstall(InstallTypeDto dto) {
		
		int runStatus = installService.getRunnerStatus() ; 
		Assert.isTrue(runStatus == Const.PRE , "ACP服务正常安装中...") ;
		
		installService.initInstallStatus() ; 
		
		return this.install(dto) ; 
	}

	/**
	 * 运行安装
	 * 
	 * @return
	 */
	@ApiOperation(value = "运行安装")
	@GetMapping("/install")
	public AjaxResult install(InstallTypeDto dto) {

		log.debug("运行安装:{}", dto.toString());
		
		Assert.hasLength(dto.getType() , "安装方式不能为空.") ;
		Assert.isTrue(InstallType.DOCKER.getTypeList().contains(dto.getType()) , "安装方式不存在");
		
		try {
			installService.installAcp(dto) ; 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return AjaxResult.success();
	}

	/**
	 * 获取到安装项
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取到安装项")
	@GetMapping("/getInstallItem")
	public AjaxResult getInstallItem() {
		
		List<InstallItemDto> items = new ArrayList<InstallItemDto>() ; 

		stepText[] arr = stepText.values() ; 
		
		for(stepText i : arr) {
		
			InstallItemDto item = new InstallItemDto() ; 
			
			item.setLabel(i.getStep()) ; 
			item.setName(i.getText()) ; 
			item.setStatus(Const.PRE) ; 
			
			items.add(item) ; 
		}

		return AjaxResult.success(items);
	}

}
