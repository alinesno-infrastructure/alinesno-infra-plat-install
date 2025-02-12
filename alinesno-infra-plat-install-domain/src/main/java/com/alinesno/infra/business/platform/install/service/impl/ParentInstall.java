package com.alinesno.infra.business.platform.install.service.impl;

import com.alinesno.infra.business.platform.install.dto.CheckEnvDto;
import com.alinesno.infra.business.platform.install.dto.InstallForm;
import com.alinesno.infra.business.platform.install.dto.project.AipBean;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.service.IInstallService;
import com.alinesno.infra.business.platform.install.shell.domain.CmdResult;
import com.alinesno.infra.business.platform.install.shell.runner.Log;
import com.alinesno.infra.business.platform.install.shell.runner.LogListener;
import com.alinesno.infra.business.platform.install.shell.runner.ProcListener;
import com.alinesno.infra.business.platform.install.utils.AipConfigDownUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.lang.exception.RpcServiceRuntimeException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ParentInstall implements IInstallService {

    @Autowired
    protected AipConfigDownUtils aipConfig ;

    @Override
    public void install(InstallForm installForm) {
        if(installForm.getEnvType().equals("DockerCompose")){
            installByDockerCompose(installForm);
        }else if(installForm.getEnvType().equals("kubernetes")){
            installByK8s(installForm);
        }else {
            throw new RuntimeException("环境类型错误");
        }
    }

    @NotNull
    protected List<Project> getProjectYamlList(InstallForm installForm) throws IOException {
        // 根据版本号下载安装程序
        log.debug("根据版本号:{}解析下载安装程序..." , installForm.getVersion());
        AipBean aipBean = aipConfig.config(installForm);

        List<Project> projects = new ArrayList<>() ;

        // 解析Bean获取到安装文件
        List<String> projectYamlList = new ArrayList<>() ;

        for (Project project : aipBean.getProjects()) {
            if(installForm.getPlatformType().contains(project.getCode())){

                log.debug("开始下载项目Yaml配置文件:{}" , project.getName());
                List<String> ps = aipConfig.downloadProjectYaml(installForm ,  project) ;
                projectYamlList.addAll(ps) ;

                projects.add(project) ;
            }
        }

        projectYamlList.forEach(p -> {
            log.debug("下载项目Yaml配置文件:{}" , p);
        });

        log.debug("下载项目配置文件完成...");
        return projects;
    }

    /**
     * 通过dockerCompose安装
     * @param installForm
     */
    void installByDockerCompose(InstallForm installForm){};

    /**
     * 通过k8s安装
     * @param installForm
     */
    void installByK8s(InstallForm installForm){};

    protected static class NullProcListener implements ProcListener {

        @Override
        public void onStarted(CmdResult result) {
            System.out.println("---> onStarted ,  result = " + result);
            log.info("开始运行安装脚本:{}" , result);
        }

        @Override
        public void onLogged(CmdResult result) {
            log.debug("---> onLogged ,  result = {}" , result);
        }

        @Override
        public void onExecuted(CmdResult result) {
            if(result != null && result.getExitValue() != 0) {
                System.out.println("---> onExecuted ,  result = " + result);
                log.error("运行脚本异常:{}", result);
            }
        }

        @Override
        public void onException(CmdResult result) {
            if(result != null && result.getExitValue() != 0){
                System.out.println("---> onException ,  result = " + result);
                log.error("运行脚本异常:{}" , result.toString());
            }
        }
    }

    protected final static LogListener logListener = new LogListener() {

        private boolean isFinish = false;
        private String cmdLogContent = null;

        @Override
        public void onLog(Log logger) {
            cmdLogContent = logger.getContent();
            log.info(cmdLogContent);
            isFinish = false;
        }

        @Override
        public void onFinish() {
            System.out.println("读写完成.");
            log.info("脚本运行结束.");
            isFinish = true;
        }

        public boolean isFinish() {
            return isFinish;
        }

        @Override
        public String cmdLogContent() {
            return cmdLogContent;
        }

    };

}
