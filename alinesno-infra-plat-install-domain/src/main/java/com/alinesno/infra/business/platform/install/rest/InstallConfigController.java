package com.alinesno.infra.business.platform.install.rest;

import com.alinesno.infra.business.platform.install.dto.CheckEnvDto;
import com.alinesno.infra.business.platform.install.dto.InstallForm;
import com.alinesno.infra.business.platform.install.service.IInstallService;
import com.alinesno.infra.common.facade.response.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/install")
public class InstallConfigController {

    @Autowired
    private IInstallService installerService;

    // 接收安装配置信息
    @PostMapping("/submit")
    public AjaxResult submitInstallConfig(@Valid @RequestBody InstallForm installForm) {
        try {
            Thread.sleep(2000);
            log.debug("Received install configuration: " + installForm);

            log.debug("开始安装程序...");
            installerService.installByK8s(installForm);
            log.debug("安装程序安装完成！");

            return AjaxResult.success() ;
        } catch (Exception e) {
            log.error("提交安装配置时发生错误：" + e.getMessage(), e);
            return AjaxResult.error() ;
        }
    }

    @GetMapping("/check-environment")
    public AjaxResult checkEnvironment() {
        try {
            CheckEnvDto  dto = installerService.checkEnvironment();

            log.debug("环境检查结束！") ;

            return AjaxResult.success(dto) ;
        } catch (Exception e) {
            log.error("环境检查时发生错误：" + e.getMessage(), e);
            return AjaxResult.error() ;
        }
    }
}

