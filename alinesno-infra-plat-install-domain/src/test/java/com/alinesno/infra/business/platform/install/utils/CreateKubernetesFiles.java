package com.alinesno.infra.business.platform.install.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CreateKubernetesFiles {

    private static final String KUBERNETES_DEV_YAML = "# 创建service为${PROJECT_NAME}\n" +
            "apiVersion: v1\n" +
            "kind: Service\n" +
            "metadata:\n" +
            "  name: ${PROJECT_NAME}\n" +
            "  namespace: ${NAMESPACE}\n" +
            "spec:\n" +
            "  selector:\n" +
            "    app: ${PROJECT_NAME}\n" +
            "    release: canary\n" +
            "  ports:\n" +
            "    - name: http\n" +
            "      targetPort: 8080\n" +
            "      port: 8080\n" +
            "---\n" +
            "# 创建后端服务的pod\n" +
            "apiVersion: apps/v1\n" +
            "kind: Deployment\n" +
            "metadata:\n" +
            "  name: ${PROJECT_NAME}\n" +
            "  namespace: ${NAMESPACE}\n" +
            "spec:\n" +
            "  replicas: 1\n" +
            "  selector:\n" +
            "    matchLabels:\n" +
            "      app: ${PROJECT_NAME}\n" +
            "      release: canary\n" +
            "  template:\n" +
            "    metadata:\n" +
            "      labels:\n" +
            "        app: ${PROJECT_NAME}\n" +
            "        release: canary\n" +
            "    spec:\n" +
            "      imagePullSecrets:\n" +
            "        - name: aliyun-docker-registry\n" +
            "      containers:\n" +
            "        - name: ${PROJECT_NAME}\n" +
            "          image: ${ALIYUN_CR_REGISTORY}/${ALIYUN_CR_NAMESPACE}/${PROJECT_NAME}:${VERSION}\n" +
            "          env:\n" +
            "            - name: JAVA_TOOL_OPTIONS\n" +
            "              value: |\n" +
            "                -Dspring.data.redis.host=${REDIS_HOST}\n" +
            "                -Dspring.data.redis.password=${REDIS_KEY}\n" +
            "                \n" +
            "                -Dspring.datasource.url=jdbc:mysql://${DB_MYSQL_URL}/dev_alinesno_infra_data_mdm_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL\n" +
            "                -Dspring.datasource.username=${DB_MYSQL_USRENAME}\n" +
            "                -Dspring.datasource.password=${DB_MYSQL_PASSWORD}\n" +
            "                \n" +
            "                -Dserver.port=8080\n" +
            "          ports:\n" +
            "            - name: http\n" +
            "              containerPort: 8080";

    private static final String KUBERNETES_ADMIN_DEV_YAML = "# 创建service为${PROJECT_NAME}\n" +
            "apiVersion: v1\n" +
            "kind: Service\n" +
            "metadata:\n" +
            "  name: ${PROJECT_NAME}\n" +
            "  namespace: ${NAMESPACE}\n" +
            "spec:\n" +
            "  selector:\n" +
            "    app: ${PROJECT_NAME}\n" +
            "    release: canary\n" +
            "  ports:\n" +
            "    - name: http\n" +
            "      targetPort: 80\n" +
            "      port: 80\n" +
            "---\n" +
            "# 创建后端服务的pod\n" +
            "apiVersion: apps/v1\n" +
            "kind: Deployment\n" +
            "metadata:\n" +
            "  name: ${PROJECT_NAME}\n" +
            "  namespace: ${NAMESPACE}\n" +
            "spec:\n" +
            "  replicas: 1\n" +
            "  selector:\n" +
            "    matchLabels:\n" +
            "      app: ${PROJECT_NAME}\n" +
            "      release: canary\n" +
            "  template:\n" +
            "    metadata:\n" +
            "      labels:\n" +
            "        app: ${PROJECT_NAME}\n" +
            "        release: canary\n" +
            "    spec:\n" +
            "      imagePullSecrets:\n" +
            "        - name: aliyun-docker-registry\n" +
            "      containers:\n" +
            "        - name: ${PROJECT_NAME}\n" +
            "          image: ${ALIYUN_CR_REGISTORY}/${ALIYUN_CR_NAMESPACE}/${PROJECT_NAME}:${VERSION}\n" +
            "          ports:\n" +
            "            - name: http\n" +
            "              containerPort: 80";

    private static final String DOCKER_COMPOSE_YAML = "version: '3.3'\n" +
            "\n" +
            "services:\n" +
            "  ${AIP_PROJECT_NAME}-boot:\n" +
            "    image: ${ALIYUN_CR_REGISTORY}/${ALIYUN_CR_NAMESPACE}/${PROJECT_NAME_BOOT}:${VERSION}\n" +
            "    environment:\n" +
            "      - JAVA_TOOL_OPTIONS=-Dspring.data.redis.host=aip_redis -Dspring.data.redis.password=aip@redis  -Dspring.datasource.url=jdbc:mysql://aip_mysql:3306/dev_alinesno_infra_data_scheduler_v100?serverTimezone=GMT%2B8&zeroDateTimeBehavior=CONVERT_TO_NULL  -Dspring.datasource.username=root -Dspring.datasource.password=aip@mysql -Dserver.port=8080 \n" +
            "    restart: always \n" +
            "\n" +
            "  ${AIP_PROJECT_NAME}-ui:\n" +
            "    image: ${ALIYUN_CR_REGISTORY}/${ALIYUN_CR_NAMESPACE}/${PROJECT_NAME_UI}:${VERSION}\n" +
            "    ports:\n" +
            "      - \"${AIP_PROJECT_UI_PORT}:80\" " ;

    public static void main(String[] args) {
        // 指定目录
        String baseDirPath = "/Users/luodong/Desktop/1.1.0-SNAPSHOT-2"; // 替换为实际路径

        // 项目列表
        List<String> servicesWithPorts = List.of(
                "alinesno-infra-base-authority:30100",
                "alinesno-infra-base-cms:30116",
                "alinesno-infra-base-config:30102",
                "alinesno-infra-base-gateway:30107",
                "alinesno-infra-base-id:30112",
                "alinesno-infra-base-identity-auth-application:30106", // Assuming it's the same as alinesno-infra-base-identity
                "alinesno-infra-base-message:30103", // Note: In your table, you wrote 'notices' instead of 'message', I assumed it's a typo.
                "alinesno-infra-base-notice:30104", // Corrected from notices
                "alinesno-infra-base-pay:30114",
                "alinesno-infra-base-platform:30120",
                "alinesno-infra-base-search:30111", // Assumed to be similar to document search
                "alinesno-infra-base-sensitive-admin:30113", // Assuming it's related to sensitive word filtering
                "alinesno-infra-base-sensitive:30113", // Same port for sensitive service
                "alinesno-infra-base-starter:30101",
                "alinesno-infra-base-storage:30105",
                "alinesno-infra-base-validate-ui:30108", // Assuming validate-ui is related to security validation
                "alinesno-infra-bus-profiling:30502",
                "alinesno-infra-bus-recommend:30501",
                "alinesno-infra-data-assets:30208",
                "alinesno-infra-data-mdm:30200",
                "alinesno-infra-data-pipeline:30202",
                "alinesno-infra-data-scheduler:30400", // Scheduler might fit better in ops category
                "alinesno-infra-ops-container:30404",
                "alinesno-infra-ops-logback:30401",
                "alinesno-infra-ops-logback-collector:30401", // Assuming collector shares the same logback service
                "alinesno-infra-ops-watcher:30405",
                "alinesno-infra-plat-console:30600",
                "alinesno-infra-plat-security:30601",
                "alinesno-infra-portal:30604", // Assuming portal is related to plat-app (mobile terminal)
                "alinesno-infra-smart-detection:30305",
                "alinesno-infra-smart-expert:30304", // Assuming expert is similar to assistant
                "alinesno-infra-smart-im:30109", // Assuming IM is instant messaging
                "alinesno-infra-smart-inference:30302", // Assuming inference is GPT reasoning
                "alinesno-infra-smart-media:30303",
                "alinesno-infra-smart-nlp:30301",
                "alinesno-infra-smart-ocr:30300"
        );

        for (String projectName : servicesWithPorts) {
            String[] parts = projectName.split(":");
            createProjectFiles(baseDirPath, parts[0], parts[1]);
        }
    }

    private static void createProjectFiles(String baseDirPath, String projectName , String port) {
        File projectDir = new File(baseDirPath, projectName);
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }

        try (FileWriter k8sDevWriter = new FileWriter(new File(projectDir, "kubernetes-dev.yaml"));
             FileWriter k8sAdminDevWriter = new FileWriter(new File(projectDir, "kubernetes-admin-dev.yaml"));
             FileWriter dockerComposeWriter = new FileWriter(new File(projectDir, "docker-compose-dev.yaml"));
             ) {

            // 将模板中的${PROJECT_NAME}替换为实际的项目名称
            String k8sDevContent = KUBERNETES_DEV_YAML ; // .replace("${PROJECT_NAME}", projectName);
            String k8sAdminDevContent = KUBERNETES_ADMIN_DEV_YAML ; // .replace("${PROJECT_NAME}", projectName);
            String dockerComposeContent = DOCKER_COMPOSE_YAML.replace("${AIP_PROJECT_NAME}" , projectName).replace("${AIP_PROJECT_UI_PORT}", port); ;

            // 写入文件
            k8sDevWriter.write(k8sDevContent);
            k8sAdminDevWriter.write(k8sAdminDevContent);
            dockerComposeWriter.write(dockerComposeContent);

            System.out.println("Created files for project: " + projectName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}