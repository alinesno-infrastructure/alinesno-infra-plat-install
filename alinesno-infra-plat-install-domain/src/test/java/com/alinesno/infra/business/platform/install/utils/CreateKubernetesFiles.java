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

    public static void main(String[] args) {
        // 指定目录
        String baseDirPath = "/Users/luodong/Desktop/1.1.0-SNAPSHOT"; // 替换为实际路径

        // 项目列表
        List<String> projects = List.of(
            "alinesno-infra-base-authority",
            "alinesno-infra-base-cms",
            "alinesno-infra-base-config",
            "alinesno-infra-base-gateway",
            "alinesno-infra-base-id",
            "alinesno-infra-base-identity-auth-application",
            "alinesno-infra-base-message",
            "alinesno-infra-base-notice",
            "alinesno-infra-base-pay",
            "alinesno-infra-base-platform",
            "alinesno-infra-base-search",
            "alinesno-infra-base-sensitive-admin",
            "alinesno-infra-base-sensitive",
            "alinesno-infra-base-starter",
            "alinesno-infra-base-storage",
            "alinesno-infra-base-validate-ui",
            "alinesno-infra-bus-profiling",
            "alinesno-infra-bus-recommend",
            "alinesno-infra-data-assets",
            "alinesno-infra-data-mdm",
            "alinesno-infra-data-pipeline",
            "alinesno-infra-data-scheduler",
            "alinesno-infra-ops-container",
            "alinesno-infra-ops-logback",
            "alinesno-infra-ops-logback-collector",
            "alinesno-infra-ops-watcher",
            "alinesno-infra-plat-console",
            "alinesno-infra-plat-security",
            "alinesno-infra-portal",
            "alinesno-infra-smart-detection",
            "alinesno-infra-smart-expert",
            "alinesno-infra-smart-im",
            "alinesno-infra-smart-inference",
            "alinesno-infra-smart-media",
            "alinesno-infra-smart-nlp",
            "alinesno-infra-smart-ocr"
        );

        for (String projectName : projects) {
            createProjectFiles(baseDirPath, projectName);
        }
    }

    private static void createProjectFiles(String baseDirPath, String projectName) {
        File projectDir = new File(baseDirPath, projectName);
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }

        try (FileWriter k8sDevWriter = new FileWriter(new File(projectDir, "kubernetes-dev.yaml"));
             FileWriter k8sAdminDevWriter = new FileWriter(new File(projectDir, "kubernetes-admin-dev.yaml"))) {

            // 将模板中的${PROJECT_NAME}替换为实际的项目名称
            String k8sDevContent = KUBERNETES_DEV_YAML ; // .replace("${PROJECT_NAME}", projectName);
            String k8sAdminDevContent = KUBERNETES_ADMIN_DEV_YAML ; // .replace("${PROJECT_NAME}", projectName);

            // 写入文件
            k8sDevWriter.write(k8sDevContent);
            k8sAdminDevWriter.write(k8sAdminDevContent);

            System.out.println("Created files for project: " + projectName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}