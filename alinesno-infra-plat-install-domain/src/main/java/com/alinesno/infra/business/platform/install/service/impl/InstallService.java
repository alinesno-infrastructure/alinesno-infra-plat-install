package com.alinesno.infra.business.platform.install.service.impl;

import com.alinesno.infra.business.platform.install.dto.*;
import com.alinesno.infra.business.platform.install.dto.project.AipBean;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import com.alinesno.infra.business.platform.install.service.IInstallService;
import com.alinesno.infra.business.platform.install.utils.AipConfigDownUtils;
import com.alinesno.infra.business.platform.install.utils.VersionCtlUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (Project project : aipBean.getProjects()) {
            log.debug("开始下载项目Yaml配置文件:{}" , project.getName());
            aipConfig.downloadProjectYaml(installForm.getVersion() , project.getName()) ;
        }
        log.debug("下载项目配置文件完成...");

        // 下载数据库文件
        aipConfig.database(installForm.getVersion());

        // 下载环境安装文件
        aipConfig.env(installForm.getVersion());

        // 执行安装基础环境(redis/minio/mysql/pgvector/elasticsearch)

    }

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
