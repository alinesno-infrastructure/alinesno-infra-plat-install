package com.alinesno.infra.business.platform.install.session;

import com.alinesno.infra.business.platform.install.dto.InstallForm;
import lombok.extern.slf4j.Slf4j;
import java.security.SecureRandom;

@Slf4j
public class InstallConfigManager {

    // 单例模式 - 饿汉式初始化
    private static final InstallConfigManager instance = new InstallConfigManager();

    // 用于存储安装配置
    private volatile InstallForm installConfig;

    // 私有构造函数，防止外部实例化
    private InstallConfigManager() {}

    // 提供全局访问点
    public static InstallConfigManager getInstance() {
        return instance;
    }

    // 设置安装配置方法（线程安全）
    public synchronized void setInstallConfig(InstallForm config) {
        // 为新增的字段生成随机密码
        generatePasswords(config);

        this.installConfig = config;
        log.info("安装配置已更新: {}", config);
    }

    // 获取安装配置方法（线程安全）
    public synchronized InstallForm getInstallConfig() {
        if (installConfig == null) {
            log.warn("尝试获取未设置的安装配置");
        }
        return installConfig;
    }

    // 辅助方法：生成随机密码
    private void generatePasswords(InstallForm form) {
        String generatedPassword = generateStrongPassword();
        form.setRedisPassword(generatedPassword);

        generatedPassword = generateStrongPassword();
        form.setMinioPassword(generatedPassword);

        generatedPassword = generateStrongPassword();
        form.setPgvectorPassword(generatedPassword);

        generatedPassword = generateStrongPassword();
        form.setElasticsearchPassword(generatedPassword);

        generatedPassword = generateStrongPassword();
        form.setMysqlPassword(generatedPassword);
    }

    // 辅助方法：生成强密码
    private String generateStrongPassword() {
        int passwordLength = 16; // 您可以根据需要调整长度
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordLength; ++i) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}