package com.alinesno.infra.business.platform.install.utils;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioUtil {

    private static final String ENDPOINT = "http://localhost:19000"; // 替换为您的MinIO服务器地址
    private static final String ACCESS_KEY = "minio";   // 替换为您的访问密钥
    private static final String SECRET_KEY = "aip@minio";   // 替换为您的秘密密钥

    private static MinioClient minioClient;

    static {
        try {
            minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
        } catch (Exception e) {
            log.error("初始化MinioClient失败", e);
        }
    }

    /**
     * 创建一个新的bucket并设置为公共可访问
     */
    public static void createPublicBucket(String bucketName) throws Exception {
        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            setBucketPublic(bucketName);
        }
    }

    /**
     * 设置bucket为公共读取权限
     */
    private static void setBucketPublic(String bucketName) throws Exception {
        // 直接使用静态字段ENDPOINT构建资源路径
        String policyJson = "{ \"Version\":\"2012-10-17\", \"Statement\":[ { \"Effect\":\"Allow\", \"Principal\":{\"AWS\":[\"*\"]}, \"Action\":[\"s3:GetObject\"], \"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"] } ] }";
        minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policyJson)
                .build());
    }

}