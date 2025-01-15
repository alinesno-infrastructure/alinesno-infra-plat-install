package com.alinesno.infra.business.platform.install.service.impl;

import com.alinesno.infra.business.platform.install.dto.*;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.shell.domain.CmdResult;
import com.alinesno.infra.business.platform.install.shell.runner.CmdExecutor;
import com.alinesno.infra.business.platform.install.shell.utils.SystemUtil;
import com.alinesno.infra.business.platform.install.utils.DatabaseUtils;
import com.alinesno.infra.business.platform.install.utils.NetUtils;
import com.alinesno.infra.business.platform.install.utils.VersionCtlUtils;
import com.alinesno.infra.common.core.monitor.Server;
import com.alinesno.infra.common.core.monitor.server.Mem;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.lang.exception.RpcServiceRuntimeException;
import java.util.List;

@Slf4j
@Service
@Primary
public class DockerComposeInstallServiceImpl extends ParentInstall {

    @SneakyThrows
    @Override
    public void installByDockerCompose(InstallForm installForm) {
        List<Project> projectYamlList = getProjectYamlList(installForm);

        printStep("安装基础工具开始.");
        // 安装基础工具
        installTools();
        printStep("安装基础工具结束.");

        // 初始化数据库脚本
        printStep("初始化数据库脚本开始.");
        DatabaseUtils.initDatabase(installForm , projectYamlList) ;
        printStep("初始化数据库脚本结束.");

        String installFilePath = NetUtils.getInstallFile() ;

        // 下载项目镜像
        int count = 1 ;
        for (Project project : projectYamlList) {
            printStep("下载项目镜像【"+project.getDesc()+"】开始.");
            downloadProjectImage(project, installFilePath , projectYamlList.size()-count);
            printStep("下载项目镜像【"+project.getDesc()+"】结束.");
            count ++ ;
        }

        count = 1 ;
        for (Project project : projectYamlList) {
            printStep("安装项目【"+project.getDesc()+"】开始.");
            runProject(project, installFilePath , projectYamlList.size() - count);
            printStep("安装项目【"+project.getDesc()+"】结束.");
        }

        // 下面是访问地址:
        log.debug("");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("AIP平台访问入口: http://{}:30109" , installForm.getServerIp());
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private void downloadProjectImage(Project project, String installFilePath, int size) {
        log.debug("开始下载项目镜像:【{}】，还有【{}】个下载" , project.getName()+":" + project.getDesc() , size);
        String projectYaml = project.getDockerComposeYamlPath();
        String shell = """
                cd %s/%s
                docker-compose -f docker-compose-dev.yaml --env-file ../.env pull
                """
                .formatted(installFilePath, project.getName()) ;

        CmdExecutor executor = new CmdExecutor(new NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(shell));
        CmdResult result = executor.run();

        log.debug("result = {}", result);
        if(result.getExitValue() != 0){
            throw new RpcServiceRuntimeException("docker-compose pull 执行失败") ;
        }
        log.debug("结束下载项目镜像:【{}】" , project.getName()+":" + project.getDesc());
    }

    private void printStep(String step){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("开始安装:{}" ,step) ;
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void runProject(Project project, String installFilePath, int size) {
        log.debug("开始启动项目:【{}】，还有【{}】个项目需要安装" , project.getName()+":" + project.getDesc() , size);

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
                # 处理ES映射本地权限的问题
                mkdir -p /usr/share/aip-env/data/elasticsearch
                chmod -R 777 /usr/share/aip-env/data/elasticsearch
                
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
    public CheckEnvDto checkEnvironment(String envType) throws Exception {

        log.debug("检查环境...");
        Assert.isTrue(!SystemUtil.isWindows() , "请使用Linux环境安装");

        // 检查内存还有硬盘是否满足
        log.debug("检查内存是否满足...");
        Server server = new Server();
        Mem mem = server.getMem();
        Assert.isTrue(mem.getTotal() < 32 * 1024 * 1024 * 1024L , "内存不足，请至少32G内存");

        CheckEnvDto checkEnvDto = new CheckEnvDto();

        DockerInfoDto dockerInfo = VersionCtlUtils.dockerInfo();
        log.debug("dockerInfo:{}", dockerInfo);

        NetInfoDto netInfo = VersionCtlUtils.netInfo();
        log.debug("netInfo:{}", netInfo);

        // 查询Docker-Compose是否存在，还有版本号
        DockerComposeInfoDto dockerComposeInfo = VersionCtlUtils.dockerComposeInfo();
        log.debug("dockerComposeInfo:{}", dockerComposeInfo);

        String shell = "docker-compose -v";
        CmdExecutor executor = new CmdExecutor(new ParentInstall.NullProcListener(), logListener, null, null, Lists.newArrayList("K8S_SHELL_RUNNER"), null, Lists.newArrayList(shell));
        CmdResult result = executor.run();
        if(result.getExitValue() != 0){
            throw new RpcServiceRuntimeException("docker-compose 请检查安装是否正确") ;
        }

        checkEnvDto.setDockerInfo(dockerInfo);
        checkEnvDto.setDockerComposeInfo(dockerComposeInfo);
        checkEnvDto.setNetInfo(netInfo);

        return checkEnvDto;

    }

}
