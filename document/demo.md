pg:
image: pgvector/pgvector:0.7.0-pg15 # docker hub
container_name: pg
restart: always
ports: 
- 5432:5432
environment:
- POSTGRES_USER=username
- POSTGRES_PASSWORD=password
- POSTGRES_DB=postgres


mysql:
image: mysql:8.0.36
container_name: mysql
restart: always
ports:
- 3306:3306
command: --default-authentication-plugin=mysql_native_password
environment:
MYSQL_ROOT_PASSWORD: adminer 
MYSQL_DATABASE: alinesno_database

redis:
  image: registry.cn-shenzhen.aliyuncs.com/alinesno-infra-base/redis:5.0.3-8.6
  command: ["redis-server", "--requirepass", "${REDIS_KEY}"]