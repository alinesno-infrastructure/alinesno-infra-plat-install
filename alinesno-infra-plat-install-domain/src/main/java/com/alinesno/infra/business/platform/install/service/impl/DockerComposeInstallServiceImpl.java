package com.alinesno.infra.business.platform.install.service.impl;

import com.alinesno.infra.business.platform.install.dto.*;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.shell.domain.CmdResult;
import com.alinesno.infra.business.platform.install.shell.runner.CmdExecutor;
import com.alinesno.infra.business.platform.install.utils.DatabaseUtils;
import com.alinesno.infra.business.platform.install.utils.NetUtils;
import com.alinesno.infra.business.platform.install.utils.VersionCtlUtils;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.lang.exception.RpcServiceRuntimeException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Primary
public class DockerComposeInstallServiceImpl extends ParentInstall {

    @SneakyThrows
    @Override
    public void installByDockerCompose(InstallForm installForm) {
        List<Project> projectYamlList = getProjectYamlList(installForm);

        // 安装基础工具
        installTools();

        // 初始化数据库脚本
        DatabaseUtils.initDatabase(installForm , projectYamlList) ;

        String installFilePath = NetUtils.getInstallFile() ;

        for (Project project : projectYamlList) {
            runProject(project, installFilePath);
        }
    }

    private static void runProject(Project project, String installFilePath) {
        log.debug("开始启动项目:【{}】" , project.getName()+":" + project.getDesc());

        String projectYaml = project.getDockerComposeYamlPath();
        String shell = """
                cd %s/%s
                docker-compose -f docker-compose-dev.yaml --env-file ../.env down
                sleep 10
                
                # 启动服务
                docker-compose -f docker-compose-dev.yaml --env-file ../.env up -d
                docker-compose -f docker-compose-dev.yaml --env-file ../.env up --wait
                
                docker-compose -f docker-compose-dev.yaml --env-file ../.env ps
                """
                .formatted(installFilePath, project.getName()) ;

        CmdExecutor executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(shell));
        CmdResult result = executor.run();

        log.debug("result = {}", result);
        if(result.getExitValue() != 0){
            throw new RpcServiceRuntimeException("docker-compose shell 执行失败") ;
        }

        log.debug("启动项目完成:【{}】" , project.getName()+":" + project.getDesc());
    }

    public void installTools() {
        String installFilePath = NetUtils.getInstallFile() ;
        String cdShell = "cd " + installFilePath ;
        String shell = """
                docker-compose -f alinesno-env-tools.yaml down
                sleep 10
                
                # 启动服务
                docker-compose -f alinesno-env-tools.yaml up -d
                docker-compose -f alinesno-env-tools.yaml up --wait
                
                docker-compose -f alinesno-env-tools.yaml ps
                """ ;
        log.debug("docker-compose shell = {}", shell);

        CmdExecutor executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(cdShell , shell));
        CmdResult result = executor.run();

        log.debug("result = {}", result);
        if(result.getExitValue() != 0){
            throw new RpcServiceRuntimeException("docker-compose shell 执行失败") ;
        }

        log.debug("初始环境安装执行完成.");
    }

    @Override
    public CheckEnvDto checkEnvironment() throws Exception {

        log.debug("检查环境...");

        CheckEnvDto checkEnvDto = new CheckEnvDto();

        DockerInfoDto dockerInfo = VersionCtlUtils.dockerInfo();
        log.debug("dockerInfo:{}", dockerInfo);

        NetInfoDto netInfo = VersionCtlUtils.netInfo();
        log.debug("netInfo:{}", netInfo);

        // 查询Docker-Compose是否存在，还有版本号
        DockerComposeInfoDto dockerComposeInfo = VersionCtlUtils.dockerComposeInfo();
        log.debug("dockerComposeInfo:{}", dockerComposeInfo);

        // 检查kubernetes是否存在，还有版本号
        KubectlInfoDto kubernetesInfo = VersionCtlUtils.kubernetesInfo();
        log.debug("kubernetesInfo:{}", kubernetesInfo);

        checkEnvDto.setDockerInfo(dockerInfo);
        checkEnvDto.setDockerComposeInfo(dockerComposeInfo);
        checkEnvDto.setKubernetesInfo(kubernetesInfo);
        checkEnvDto.setNetInfo(netInfo);

        return checkEnvDto;

    }

}
