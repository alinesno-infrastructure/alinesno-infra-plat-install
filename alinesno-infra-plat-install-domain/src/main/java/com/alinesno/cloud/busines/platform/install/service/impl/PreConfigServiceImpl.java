package com.alinesno.cloud.busines.platform.install.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.lang.exception.RpcServiceRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alinesno.cloud.busines.platform.install.constants.Const;
import com.alinesno.cloud.busines.platform.install.gateway.dto.CheckEnvDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.ConfigDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerComposeInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.DockerInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.KubectlInfoDto;
import com.alinesno.cloud.busines.platform.install.gateway.dto.NetInfoDto;
import com.alinesno.cloud.busines.platform.install.service.IPreConfigService;
import com.alinesno.cloud.busines.platform.install.session.Db;
import com.alinesno.cloud.busines.platform.install.utils.VersionCtlUtils;

import io.minio.MinioClient;
import redis.clients.jedis.Jedis;

/**
 * 配置服务是否正确验证
 * 
 * @author luoxiaodong
 * @version 1.0.0
 */
@Service
public class PreConfigServiceImpl<MinIoProperties> implements IPreConfigService {

	private static final Logger log = LoggerFactory.getLogger(PreConfigServiceImpl.class);

	@Override
	public void checkConfig(ConfigDto configDto) {

		log.debug("config Dto = {}", configDto);

		// 验证数据库是否正确
		validateMysql(configDto.getDatabaseUrl(), configDto.getDatabaseUser(), configDto.getDatabasePwd());

//		// 验证redis是否正确
//		validateRedis(configDto.getRedisHost(), configDto.getRedisPort(), configDto.getRedisPwd());


		// 验证minio是否正确
		validateMinio(configDto.getMinioUrl(), configDto.getMinioUser(), configDto.getMinioPwd());

		// 把配置放到map中
		Db.Config.put(Const.CONFIG_MYSQL_URL, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_MYSQL_USERNAME, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_MYSQL_PASSWORD, configDto.getDatabaseUrl());

		Db.Config.put(Const.CONFIG_REDIS_HOST, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_REDIS_PORT, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_REDIS_PASSORD, configDto.getDatabaseUrl());

		Db.Config.put(Const.CONFIG_MINIO_ENDPOINT, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_MINIO_KEY, configDto.getDatabaseUrl());
		Db.Config.put(Const.CONFIG_MINIO_SECRET, configDto.getDatabaseUrl());

		Db.Config.put(Const.CONFIG_DOMAIN_HOST, configDto.getDomainHost());

	}

	/**
	 * 验证数据库是否正确
	 *
	 * @param databaseUrl
	 * @param databaseUser
	 * @param databasePwd
	 */
	private void validateMysql(String databaseUrl, String databaseUser, String databasePwd) {

		Assert.hasLength(databaseUrl, "数据库地址不能为空");
		Assert.hasLength(databaseUser, "数据库用户名不能为空");
		Assert.hasLength(databasePwd, "数据库密码不能为空");

		String driver = "com.mysql.jdbc.Driver"; // 获取mysql数据库的驱动类
		Connection conn = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(databaseUrl, databaseUser, databasePwd);// 获取连接对象

			log.debug("connection = {}", conn);

		} catch (Exception e) {
			e.printStackTrace();
			// 把异常抛出来
			throw new RpcServiceRuntimeException("数据库[" + databaseUrl + "] , username:[" + databaseUser + "]连接异常");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 验证redis连接是否正常
	 *
	 * @param
	 * @param redisPort
	 */
	private void validateRedis(String redisHost, String redisPort, String redisPwd) {

		Assert.hasLength(redisHost, "redis地址不能为空");
		Assert.hasLength(String.valueOf(redisPort), "redis端口不能为空");
		Assert.hasLength(redisPwd, "redis密码不能为空");

		try {

			Jedis jedis = new Jedis(redisHost, Integer.parseInt(redisPort));

			log.debug("jedis = {}", jedis);

			jedis.auth(redisPwd); // 密码
			jedis.ping();
			System.out.println("redis连接成功");
		} catch (Exception e) {
			throw new RpcServiceRuntimeException("Redis " + redisHost + ":" + redisPort + " 连接异常");
		}

	}

	/**
	 * 验证Minio是否正确
	 *
	 * @param minioUrl
	 * @param minioUser
	 * @param minioPwd
	 */

	@SuppressWarnings("deprecation")
	public void validateMinio(String minioUrl, String minioUser, String minioPwd) {

		Assert.hasLength(minioUrl, " minio保存地址不能为空");
		Assert.hasLength(minioUser, "minio账号名不能为空");
		Assert.hasLength(minioPwd, "minio密码不能为空");

		MinioClient minioClient = null;
		try {
			String testBucket = "alinesno-test-bucket"; // 验证bucket

			minioClient = new MinioClient(minioUrl, minioUser, minioPwd);
			log.debug("minioClient = {}", minioClient);

			// 验证创建文件
			boolean isExist = minioClient.bucketExists(testBucket);
			if (!isExist) {
				minioClient.makeBucket(testBucket);
			}

		} catch (Exception e) {
			log.error("连接minio异常:{}", e);
			throw new RpcServiceRuntimeException("初始化minio配置异常, 连接【" + minioUrl + "】");
		} finally {
			if (minioClient != null) {
				minioClient.disableAccelerateEndpoint(); // 断开连接
			}
		}

	}

	@Override
	public Map<String, String> getCurrentConfig() {
		// 初始化配置
		return Db.Config;
	}

	/**
	 * 检查环境配置
	 * @return
	 */
	@Override
	public CheckEnvDto checkEnvInfo(CheckEnvDto checkEnvDto) {

		// 获取环境变量
		DockerComposeInfoDto dockerComposeInfoDto = VersionCtlUtils.dockerComposeInfo();
		DockerInfoDto dockerInfoDto = VersionCtlUtils.dockerInfo();
		KubectlInfoDto kubectlInfoDto = VersionCtlUtils.kubernetesInfo();
		NetInfoDto netInfoDto = VersionCtlUtils.netInfo();

		// 返回环境配置
		checkEnvDto.setDockerComposeInfo(dockerComposeInfoDto);
		checkEnvDto.setDockerInfo(dockerInfoDto);
		checkEnvDto.setKubernetesInfo(kubectlInfoDto);

		return checkEnvDto ;
	}

}
