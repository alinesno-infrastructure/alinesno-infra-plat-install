package com.alinesno.infra.business.platform.install.service;

import com.alinesno.infra.business.platform.install.dto.CheckEnvDto;
import com.alinesno.infra.business.platform.install.dto.InstallForm;

/**
 * 安装服务
 */
public interface IInstallService {

    /**
     * 安装
     * @param installForm
     */
    void install(InstallForm installForm);

    /**
     * 检查环境
     *
     * @return
     */
    CheckEnvDto checkEnvironment() throws Exception;
}
