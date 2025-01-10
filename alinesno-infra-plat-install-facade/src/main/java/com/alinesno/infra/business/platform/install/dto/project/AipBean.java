package com.alinesno.infra.business.platform.install.dto.project;

import lombok.Data;
import net.sf.jsqlparser.schema.Database;

import java.util.List;

/**
 * Aip类代表了一个安装项目的数据传输对象
 * 它包含了安装项目的基本信息，如版本号、数据库配置以及项目列表
 */
@Data
public class AipBean {

    /**
     * 版本号，表示安装项目的版本
     */
    private String version;

    /**
     * 数据库配置，用于指定安装项目所需的操作的数据库信息
     */
    private Database database;

    /**
     * 项目列表，包含了一组待安装的项目对象
     */
    private List<Project> projects;
}
