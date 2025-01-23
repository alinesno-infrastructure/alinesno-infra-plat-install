package com.alinesno.infra.business.platform.install.utils;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.business.platform.install.constants.Const;
import com.alinesno.infra.business.platform.install.dto.InstallForm;
import com.alinesno.infra.business.platform.install.dto.project.AipBean;
import com.alinesno.infra.business.platform.install.dto.project.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * AIP下载工具类
 */
@Slf4j
@Component
public class AipConfigDownUtils {

    private final String installPath = NetUtils.getInstallFile() ;

    public AipBean config(InstallForm installForm) throws IOException {  // 下载配置文件

        String version = installForm.getVersion() ;

        // 清理当前目录文件
        FileUtils.forceMkdir(new File(installPath));

        String downloadUrl = Const.qiniuDomain + File.separator + version + "/aip-config.json" ;
        String downloadEnvUrl = Const.qiniuDomain + File.separator + version + "/.env" ;
        String toolsEnvUrl = Const.qiniuDomain + File.separator + version + "/alinesno-env-tools.yaml" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl, installPath + File.separator + "aip-config.json") ;
        NetUtils.download(downloadEnvUrl, installPath + File.separator + ".env") ;
        NetUtils.download(toolsEnvUrl, installPath + File.separator + "alinesno-env-tools.yaml") ;
        log.debug("下载配置文件成功:{}" , installPath);

        // 写入环境变量到.env文件当中
        // QIANWEN_SK=xxx
        String env = FileUtils.readFileToString(new File(installPath + File.separator + ".env") , Charset.defaultCharset());
        env = env.replace("QIANWEN_SK=" , "QIANWEN_SK=" + installForm.getApiKey()) ;
        env = env.replace("${USER_HOME}" , System.getProperty("user.home")) ;
        env = env.replace("host.docker.internal" , installForm.getServerIp()) ;
        FileUtils.writeStringToFile(new File(installPath + File.separator + ".env") , env , Charset.defaultCharset()) ;

        String aipConfigJsonStr = FileUtils.readFileToString(new File(installPath + File.separator + "aip-config.json") , Charset.defaultCharset());
        log.debug("开始解析配置文件:\r\n{}" , aipConfigJsonStr);

        return JSONObject.parseObject(aipConfigJsonStr , AipBean.class);
    }

    public List<String> downloadProjectYaml(InstallForm installForm, Project project) throws IOException{

        String name = project.getName() ;
        String version = installForm.getVersion() ;
        String database = project.getDatabase() ;

        List<String> projectFileList = new ArrayList<>() ;

        String projectDir = installPath + File.separator + name ;
        FileUtils.forceMkdir(new File(projectDir)) ;

        String dockerComposeDownloadUrl = Const.qiniuDomain + File.separator + version + File.separator + name + File.separator +"docker-compose-dev.yaml" ;

        String bootDownloadUrl = Const.qiniuDomain + File.separator + version + File.separator + name + File.separator +"kubernetes-dev.yaml" ;
        String uiDownloadUrl = Const.qiniuDomain + File.separator + version + File.separator + name + File.separator +"kubernetes-admin-dev.yaml" ;

        String dockerComposeFileName = projectDir + File.separator + "docker-compose-dev.yaml" ;
        String bootFileName = projectDir + File.separator + "kubernetes-dev.yaml" ;
        String uiFileName = projectDir + File.separator + "kubernetes-admin-dev.yaml" ;

        if(StringUtils.isNotBlank(database)){
            String databaseUrl = Const.qiniuDomain + File.separator + version + File.separator +  database ;
            String databaseFileName = installPath + File.separator + database ;
            NetUtils.download(databaseUrl , databaseFileName) ;
            project.setDatabaseSqlPath(databaseFileName);
            log.debug("下载数据库文件:{} , 成功:{}" , database , databaseFileName);

            String myConf = """
                    [mysqld]
                    # 基础设置
                    user=mysql
                    pid-file=/var/run/mysqld/mysqld.pid
                    socket=/var/run/mysqld/mysqld.sock
                    datadir=/var/lib/mysql
                                        
                    # 性能相关设置
                    innodb_buffer_pool_size=4G  # 根据实际情况调整，建议设为物理内存的70%-80%
                    innodb_log_file_size=256M   # InnoDB 日志文件大小，增加可以提高写入性能
                    innodb_flush_method=O_DIRECT
                    innodb_flush_log_at_trx_commit=1 # 生产环境中推荐设置为1以保证事务一致性
                    innodb_thread_concurrency=0      # 让InnoDB自动管理线程并发度
                    innodb_read_io_threads=64        # 读取I/O线程数
                    innodb_write_io_threads=64       # 写入I/O线程数
                    innodb_io_capacity=2000          # SSD硬盘下可以适当增大此值
                                        
                    # 连接相关设置
                    max_connections=500              # 最大连接数
                    wait_timeout=600                 # 非交互式会话超时时间（秒）
                    interactive_timeout=600          # 交互式会话超时时间（秒）
                    connect_timeout=30               # 连接超时时间（秒）
                    net_read_timeout=60              # 读取超时时间（秒）
                    net_write_timeout=60             # 写入超时时间（秒）
                    max_allowed_packet=128M          # 允许的最大包大小
                                        
                    # 复制和日志相关设置
                    log_error=/var/log/mysql/error.log
                    slow_query_log=1
                    slow_query_log_file=/var/log/mysql/mysql-slow.log
                    long_query_time=2                # 慢查询阈值（秒）
                    log_bin=mysql-bin                # 开启二进制日志
                    binlog_format=ROW                # 推荐使用行级复制格式
                    expire_logs_days=10              # 二进制日志过期天数
                                        
                    # 网络设置
                    port=3306                        # 默认端口
                    skip_name_resolve                # 禁用DNS反向解析，减少延迟
                    """;
            FileUtils.writeStringToFile(new File(installPath + File.separator + "aip-env" + File.separator + "my.cnf") , myConf , Charset.defaultCharset()) ;
        }

        if(installForm.getEnvType().equals("DockerCompose")){
            NetUtils.download(dockerComposeDownloadUrl , dockerComposeFileName) ;
            projectFileList.add(dockerComposeFileName) ;
            project.setDockerComposeYamlPath(dockerComposeFileName);
        }else if(installForm.getEnvType().equals("kubernetes")){
            NetUtils.download(bootDownloadUrl , bootFileName) ;
            NetUtils.download(uiDownloadUrl , uiFileName) ;
            projectFileList.add(bootFileName) ;
            projectFileList.add(uiFileName) ;
        }

        log.debug("下载项目:{} , 文件成功:{}" , name , projectDir);

        return projectFileList ;
    }

    public String database(String version) {
        log.debug("开始下载数据库文件...");
        String downloadUrl = Const.qiniuDomain + File.separator + version + "/alinesno-database.sql" ;
        String fileName = installPath + File.separator + "alinesno-database.sql" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl, fileName) ;
        log.debug("下载配置文件成功:{}" , installPath);

        return fileName ;
    }

    public String env(String version) {
        String downloadUrl = Const.qiniuDomain + File.separator + version + "/alinesno-env-tools.yaml" ;
        String fileName = installPath + File.separator + "alinesno-env-tools.yaml" ;

        log.debug("开始下载配置文件:{}" , downloadUrl);
        NetUtils.download(downloadUrl,fileName) ;
        log.debug("下载配置文件成功:{}" , installPath);

        return fileName ;
    }
}
