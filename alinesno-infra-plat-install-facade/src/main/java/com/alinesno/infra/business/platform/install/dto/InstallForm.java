package com.alinesno.infra.business.platform.install.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 安装表单实体类
 * 用于接收和处理安装过程中从前端传来的参数信息
 */
@ToString
@Data
public class InstallForm {

    /**
     * 版本号
     * 用于指定当前安装的版本
     */
    private String version;

    /**
     * API密钥
     * 用于验证安装过程中的身份信息
     */
    private String apiKey;

    /**
     * 管理员用户名
     * 用于创建初始管理员账户
     */
    private String adminUsername;

    /**
     * 管理员密码
     * 用于创建初始管理员账户
     */
    private String adminPassword;

    /**
     * 环境类型
     * 用于指定当前安装的环境，如开发、测试或生产环境
     */
    private String envType;

    /**
     * 服务器IP
     * 用于指定安装后的服务器访问地址
     */
    private String serverIp;

    /**
     * 访问端口
     * 用于指定安装后的服务器访问端口
     */
    private String accessPort;

    /**
     * 是否保存配置
     * 用于决定是否在安装过程中保存配置信息
     */
    private boolean saveConfig;

    /**
     * 是否加密
     * 用于决定是否对敏感信息进行加密处理
     */
    private boolean isEncrypt;

    // 增加redis密码/minio密码/pgvector密码/elasticsearch密码/mysql密码
    private String redisPassword;

    private String minioPassword;

    private String pgvectorPassword;

    private String elasticsearchPassword;

    private String mysqlPassword;

}
