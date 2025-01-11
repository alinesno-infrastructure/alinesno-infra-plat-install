package com.alinesno.infra.business.platform.install.service.impl;

import com.alinesno.infra.business.platform.install.constants.AIPYaml;
import com.alinesno.infra.business.platform.install.constants.Const;
import com.alinesno.infra.business.platform.install.dto.*;
import com.alinesno.infra.business.platform.install.dto.project.AipBean;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.service.IInstallService;
import com.alinesno.infra.business.platform.install.shell.domain.CmdResult;
import com.alinesno.infra.business.platform.install.shell.runner.CmdExecutor;
import com.alinesno.infra.business.platform.install.shell.runner.Log;
import com.alinesno.infra.business.platform.install.shell.runner.LogListener;
import com.alinesno.infra.business.platform.install.shell.runner.ProcListener;
import com.alinesno.infra.business.platform.install.utils.AipConfigDownUtils;
import com.alinesno.infra.business.platform.install.utils.DatabaseUtils;
import com.alinesno.infra.business.platform.install.utils.TemplateUtils;
import com.alinesno.infra.business.platform.install.utils.VersionCtlUtils;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.exception.RpcServiceRuntimeException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InstallService implements IInstallService {

    @Autowired
    private AipConfigDownUtils aipConfig ;

    @Override
    public void install(InstallForm installForm) {

    }

    @SneakyThrows
    @Override
    public void installByK8s(InstallForm installForm) {
        log.debug("开始安装程序，使用k8s安装...");

        // 根据版本号下载安装程序
        log.debug("根据版本号:{}解析下载安装程序..." , installForm.getVersion());
        AipBean aipBean = aipConfig.config(installForm.getVersion());

        // 解析Bean获取到安装文件
        List<String> projectYamlList = new ArrayList<>() ;
        for (Project project : aipBean.getProjects()) {
            log.debug("开始下载项目Yaml配置文件:{}" , project.getName());
            List<String> ps = aipConfig.downloadProjectYaml(installForm.getVersion() , project.getName()) ;
            projectYamlList.addAll(ps) ;
        }
        log.debug("下载项目配置文件完成...");

        // 下载数据库文件
        String databaseSql =  aipConfig.database(installForm.getVersion());

        log.debug("创建命名空间...");
        try{
            String nameSpaceYaml = createK8SNamespace();
            CmdExecutor executor = new CmdExecutor(new NullProcListener(),
                    logListener,
                    null,
                    null,
                    Lists.newArrayList("K8S_SHELL_RUNNER"),
                    null,
                    Lists.newArrayList(nameSpaceYaml));
            CmdResult result = executor.run();
        }catch(Exception e){
            log.warn("命名空间已存在...");
        }
        log.debug("创建命名空间完成...");

        // 下载环境安装文件
//        String envK8SYaml = aipConfig.env(installForm.getVersion());
//
//        // 执行安装基础环境(redis/minio/mysql/pgvector/elasticsearch)
//        log.debug("开始安装基础环境...");
//        // 替换环境变量
//        TemplateUtils.renderTemplate(envK8SYaml, installForm);
//        String k8sApplyShell = getK8SApplyShell(envK8SYaml) ;
//        CmdExecutor executor = new CmdExecutor(new NullProcListener(),
//                logListener,
//                null,
//                null,
//                Lists.newArrayList("K8S_SHELL_RUNNER"),
//                null,
//                Lists.newArrayList(k8sApplyShell));
//
//        CmdResult result = executor.run();
//        log.debug("result = {}" , result);
//        if (result.getExitValue() != 0) {
//            log.error("安装基础环境失败...");
//            throw new RpcServiceRuntimeException("安装基础环境失败") ;
//        }

        // 执行安装项目到配置环境中
        log.debug("开始安装项目...");
        for (String projectYaml : projectYamlList) {
            CmdExecutor executor = new CmdExecutor(new NullProcListener(),
                    logListener,
                    null,
                    null,
                    Lists.newArrayList("K8S_SHELL_RUNNER"),
                    null,
                    Lists.newArrayList(projectYaml));

            CmdResult result = executor.run();
            log.debug("result = {}" , result);
            if (result.getExitValue() != 0) {
                log.error("安装项目{}失败..." , projectYaml);
                throw new RpcServiceRuntimeException("安装项目"+projectYaml+"失败") ;
            }
        }
        log.debug("安装项目完成...");

        // 执行安装数据库到配置环境中
        log.debug("开始安装数据库...");
        DatabaseUtils.importDatabase(databaseSql);
        log.debug("安装数据库完成...");

        // 安装完善检查环境
        log.debug("安装完善检查环境...");

        // 返回安装配置

    }

    /**
     * 运行shell命令
     *
     * @return
     */
    public String getK8SApplyShell(String installFilePath) {
        String shell = "kubectl apply -f " + installFilePath + " --insecure-skip-tls-verify" ;
        log.debug("k8s shell = {}" , shell);
        return shell;
    }

    /**
     * 创建表K8S命名空间
     */
    public String createK8SNamespace() {
        String k8sNamespace = "alinesno-infrastructure-platform" ;
        String shell = "kubectl create namespace " + k8sNamespace + " --insecure-skip-tls-verify" ;
        log.debug("k8s shell = {}" , shell);
        return shell ;
    }

    private static class NullProcListener implements ProcListener {

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
            System.out.println("---> onExecuted ,  result = " + result);
            log.error("运行脚本异常:{}" , result);
            throw new RpcServiceRuntimeException("运行脚本异常") ;
        }

        @Override
        public void onException(CmdResult result) {
            System.out.println("---> onException ,  result = " + result);
            log.error("运行脚本异常:{}" , result);
            throw new RpcServiceRuntimeException("运行脚本异常") ;
        }
    }

    private final static LogListener logListener = new LogListener() {

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

    @Override
    public CheckEnvDto checkEnvironment() throws Exception {

        // 检查网络是否正常
        // 检查磁盘空间是否足够
        // 检查内存是否足够

        log.debug("检查环境...");

//        Server server = new Server();
//        server.copyTo();

        CheckEnvDto checkEnvDto = new CheckEnvDto();

        DockerInfoDto dockerInfo = VersionCtlUtils.dockerInfo() ;
        log.debug("dockerInfo:{}" , dockerInfo);

        NetInfoDto netInfo = VersionCtlUtils.netInfo() ;
        log.debug("netInfo:{}" , netInfo);

        // 查询Docker-Compose是否存在，还有版本号
        DockerComposeInfoDto dockerComposeInfo = VersionCtlUtils.dockerComposeInfo() ;
        log.debug("dockerComposeInfo:{}" , dockerComposeInfo);

        // 检查kubernetes是否存在，还有版本号
        KubectlInfoDto kubernetesInfo = VersionCtlUtils.kubernetesInfo() ;
        log.debug("kubernetesInfo:{}" , kubernetesInfo);

//        checkEnvDto.setServer(server);
        checkEnvDto.setDockerInfo(dockerInfo);
        checkEnvDto.setDockerComposeInfo(dockerComposeInfo);
        checkEnvDto.setKubernetesInfo(kubernetesInfo);
        checkEnvDto.setNetInfo(netInfo);

        return checkEnvDto ;

    }
}
