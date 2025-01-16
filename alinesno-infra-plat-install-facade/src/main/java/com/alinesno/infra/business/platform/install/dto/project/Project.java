package com.alinesno.infra.business.platform.install.dto.project;

import lombok.Data;

/**
 * 项目配置类
 * 用于描述项目的基本信息和配置，包括项目名称、描述、数据库配置、启动配置和UI配置
 */
@Data
public class Project {

    /**
     * 项目名称
     * 用于标识项目的唯一名称
     */
    private String name;

    /**
     * 所属套件代码
     */
    private String code ;

    /**
     * 项目描述
     * 对项目的简要介绍和说明
     */
    private String desc;

    /**
     * 数据库配置信息
     * 包含项目使用的数据库相关配置
     */
    private String database;

    /**
     * 启动配置对象
     * 包含项目启动时的配置信息
     */
    private Boot boot;

    /**
     * UI配置对象
     * 包含项目的用户界面配置信息
     */
    private Ui ui;

    /**
     * Docker Compose YAML文件路径
     */
    private String dockerComposeYamlPath ;

    /**
     * 数据库SQL文件路径
     */
    private String databaseSqlPath ;

}
