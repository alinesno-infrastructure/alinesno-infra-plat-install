package com.alinesno.infra.business.platform.install.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Boot {

    @JsonProperty("images")
    private String images;

    private int port;
}