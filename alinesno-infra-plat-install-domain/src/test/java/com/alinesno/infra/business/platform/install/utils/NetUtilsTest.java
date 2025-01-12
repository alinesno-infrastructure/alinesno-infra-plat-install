package com.alinesno.infra.business.platform.install.utils;

import org.junit.jupiter.api.Test;

public class NetUtilsTest {

    @Test
    public void testDownload(){
        String downloadUrl = "http://data.linesno.com/aip-install/1.1.0-SNAPSHOT/api-database/dump-dev_alinesno_infra_data_scheduler_v100-202501112306.sql" ;
        String localPath = "/Users/luodong/.aip-install/dump-dev_alinesno_infra_data_scheduler_v100-202501112306.sql" ;
        NetUtils.download(downloadUrl, localPath) ;
    }

}
