# alinesno-infra-platform-install
AIP平台安装服务，针对于基础服务的安装配置管理，用于基础平台能力的安装

## 集成于快速体验

- 主要针对于集成k8s服务环境的安装配置

## 基础环境镜像地址

- redis: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/redis:5.0.3-8.6
- mysql: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/mysql:8.0.36
- pgvector: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/pgvector:v0.7.0
- minio: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/minio:20250110
- elasticsearch: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/elasticsearch:8.5.2

## 基础环境启动命令

进入到docker-compose目录，修改.env的账号密码

```shell
docker-compose up -d -f alinesno-env-tools.yaml
```

本地快速启动基础环境，并验证是否正确.

获取到k8s空间的pod的镜像
```shell
kubectl get deployments -n default -o json | jq -r '.items[]|.metadata.name,(.spec.template.spec.containers[]|.name,.image)' | paste -d' ' - - - | column -t
```

运行docker-compse文件
```shell
docker-compose -f docker-compose-dev.yaml --env-file ../.env up
```