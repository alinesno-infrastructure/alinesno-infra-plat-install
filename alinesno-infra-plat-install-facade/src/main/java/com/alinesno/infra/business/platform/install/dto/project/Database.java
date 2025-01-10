package com.alinesno.infra.business.platform.install.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Database {

    @JsonProperty("file-name")
    private String fileName;
}