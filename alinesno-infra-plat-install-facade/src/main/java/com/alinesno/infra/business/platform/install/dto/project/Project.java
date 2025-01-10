package com.alinesno.infra.business.platform.install.dto.project;

import lombok.Data;

@Data
public class Project {

    private String name;
    private String desc;

    private Boot boot;

    private Ui ui;
}