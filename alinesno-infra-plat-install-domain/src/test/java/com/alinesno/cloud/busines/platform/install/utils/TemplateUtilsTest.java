package com.alinesno.cloud.busines.platform.install.utils;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.alinesno.cloud.busines.platform.install.constants.AIPYaml;
import com.alinesno.cloud.busines.platform.install.session.Db;

class TemplateUtilsTest {

	@Test
	void testWirteK8STemplate() throws IOException {
	
		TemplateUtils.writeK8STemplate(AIPYaml.k8s_base_yaml, Db.Config); 
		
	}

}
