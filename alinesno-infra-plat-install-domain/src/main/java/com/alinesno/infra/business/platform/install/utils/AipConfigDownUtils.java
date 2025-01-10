package com.alinesno.infra.business.platform.install.utils;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.business.platform.install.constants.Const;
import com.alinesno.infra.business.platform.install.dto.project.AipBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * AIP下载工具类
 */
@Slf4j
@Component
public class AipConfigDownUtils {

    private final String installPath = NetUtils.getInstallFile() ;

    public AipBean config(String version) throws IOException {  // 下载配置文件
        // 清理当前目录文件
        FileUtils.forceDeleteOnExit(new File(installPath));
        FileUtils.forceMkdir(new File(installPath));

        String downloadUrl = Const.qiniuDomain + File.separator + version + "/aip-config.json" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl, installPath + File.separator + "aip-config.json") ;
        log.debug("下载配置文件成功:{}" , installPath);

        String aipConfigJsonStr = FileUtils.readFileToString(new File(installPath + File.separator + "aip-config.json") , Charset.defaultCharset());
        log.debug("开始解析配置文件:\r\n{}" , aipConfigJsonStr);

        return JSONObject.parseObject(aipConfigJsonStr , AipBean.class);
    }

    public void downloadProjectYaml(String version , String name) throws IOException{

        String projectDir = installPath + File.separator + name ;
        FileUtils.forceMkdir(new File(projectDir)) ;

        String bootDownloadUrl = Const.qiniuDomain + File.separator + version + File.separator + name + File.separator +"kubernetes-dev.yaml" ;
        String uiDownloadUrl = Const.qiniuDomain + File.separator + version + File.separator + name + File.separator +"kubernetes-admin-dev.yaml" ;

        NetUtils.download(bootDownloadUrl , projectDir + File.separator + "kubernetes-dev.yaml");
        NetUtils.download(uiDownloadUrl , projectDir + File.separator + "kubernetes-admin-dev.yaml");

        log.debug("下载项目:{}文件成功:{}" , name , projectDir);
    }

    public void database(String version) {
        log.debug("开始下载数据库文件...");
        String downloadUrl = Const.qiniuDomain + File.separator + version + "/alinesno-database.sql" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl, installPath + File.separator + "alinesno-database.sql") ;
        log.debug("下载配置文件成功:{}" , installPath);
    }

    public void env(String version) {
        String downloadUrl = Const.qiniuDomain + File.separator + version + "/alinesno-env-tools.yaml" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl, installPath + File.separator + "alinesno-env-tools.yaml") ;
        log.debug("下载配置文件成功:{}" , installPath);
    }
}
