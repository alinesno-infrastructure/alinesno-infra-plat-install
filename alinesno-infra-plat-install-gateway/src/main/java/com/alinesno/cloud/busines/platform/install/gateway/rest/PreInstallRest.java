package com.alinesno.cloud.busines.platform.install.gateway.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.alinesno.infra.common.core.monitor.Server;
import com.alinesno.infra.common.facade.response.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.cloud.busines.platform.install.gateway.dto.CheckEnvDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.ConfigDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.ModelItemDto;
import com.alinesno.cloud.busines.platform.install.service.IPreConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 安装检查实体
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Api(tags = "安装前检查")
@RestController
@RequestMapping("/api/install/pre")
public class PreInstallRest {

	private static final Logger log = LoggerFactory.getLogger(PreInstallRest.class);

	@Autowired
	private IPreConfigService configService;

	/**
	 * 安装类型
	 */
	@ApiOperation(value = "安装类型选择")
	@GetMapping("/installCompose")
	public AjaxResult installCompose() {
		Map<String , String> map = new HashMap<String , String>() ; 
		
		return AjaxResult.success(map) ; 
	}

	/**
	 * 环境状态检测
	 * 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "环境状态检测")
	@GetMapping("/envInfo")
	public AjaxResult getInfo() throws Exception {
		Server server = new Server();
		server.copyTo();

		CheckEnvDto checkEnvDto = new CheckEnvDto();

		checkEnvDto.setServer(server);
		configService.checkEnvInfo(checkEnvDto) ; 

		log.debug("checkEnv = {}", JSONObject.toJSON(checkEnvDto));

		return AjaxResult.success(checkEnvDto);
	}

	/**
	 * 获取到安装模型
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取到安装模型")
	@GetMapping("/getInstallModel")
	public AjaxResult getInstallModel() {

		ModelItemDto item1 = new ModelItemDto();
		item1.setType("技术研发体系");
		item1.setBaseItem("包含");
		item1.setProItem("包含");
		item1.setCompanyItem("包含");

		ModelItemDto item2 = new ModelItemDto();
		item2.setType("研发中台体系");
		item2.setBaseItem("包含");
		item2.setProItem("包含");
		item2.setCompanyItem("包含");

		ModelItemDto item3 = new ModelItemDto();
		item3.setType("数据运营治理");
		item3.setBaseItem("");
		item3.setProItem("");
		item3.setCompanyItem("包含");

		ModelItemDto item4 = new ModelItemDto();
		item4.setType("自动化运维体系");
		item4.setBaseItem("");
		item4.setProItem("");
		item4.setCompanyItem("包含");

		List<ModelItemDto> items = new ArrayList<ModelItemDto>();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);

		return AjaxResult.success(items);

	}

	/**
	 * 获取当前配置
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取当前用户配置")
	@PostMapping("/getConfig")
	public AjaxResult getConfig() {

		// 获取当前配置
		Map<String, String> map = configService.getCurrentConfig();

		return AjaxResult.success(map);
	}

	/**
	 * 判断基础配置是否正确
	 * 
	 * @return
	 */
	@ApiOperation(value = "判断基础配置是否正确")
	@PostMapping("/checkConfig")
	public AjaxResult checkConfig(@RequestBody ConfigDto configDto) {

		log.debug("configDto = {}", configDto);

		configService.checkConfig(configDto);

		return AjaxResult.success();
	}

}
