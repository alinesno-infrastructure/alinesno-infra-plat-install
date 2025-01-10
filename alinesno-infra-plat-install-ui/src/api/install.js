import request from '@/utils/request'

// 提交安装配置
export function install(data) {
    console.log('data = ' + data)
  return request({
    url: '/api/install/submit',
    method: 'post',
    data: data
  })
}

// 检查安装环境
export function checkEnv() {
  return request({
    url: '/api/install/check-environment',
    method: 'get'
  })
}