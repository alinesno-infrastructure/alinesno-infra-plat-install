# 基础环境启动命令

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