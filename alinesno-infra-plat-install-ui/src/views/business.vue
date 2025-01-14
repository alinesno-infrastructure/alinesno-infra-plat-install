<template>
  <div>
    <div class="form-container">
      <el-row>
        <!-- Installation Configuration Column -->
        <el-col :span="10">
          <div class="acp-compoent-title">
            安装配置
          </div>

          <el-form 
            ref="installFormRef"
            :model="installForm" 
            :rules="rules" 
            :label-position="labelPosition" 
            v-loading="loading" 
            label-width="160px" 
            class="demo-ruleForm">

            <!-- Version Selection -->
            <el-form-item label="选择版本" prop="version">
              <el-radio-group v-model="installForm.version">
                <div v-for="(version, index) in versionOptions" :key="index" >
                  <el-radio :label="version.value" :disabled="version.disabled">{{ version.label }}</el-radio>
                  <span style="font-size: 12px; color: #909399;">{{ version.description }}</span>
                </div>
              </el-radio-group>
            </el-form-item>

            <!-- API Key Input -->
            <el-form-item label="通义Key" prop="apiKey">
              <el-input v-model="installForm.apiKey" placeholder="请输入通义千问的API Key" autocomplete="off">
                <template #suffix>
                  <a href="https://tongyi.aliyun.com/qianwen/" target="_blank" title="访问官网获取API Key">
                    <i class="el-icon-question"></i>
                  </a>
                </template>
              </el-input>
            </el-form-item>

            <!-- Admin Info -->
            <el-divider content-position="left" style="width:90%">初始管理员信息</el-divider>
            <el-form-item label="管理员账号" prop="adminUsername">
              <el-input v-model="installForm.adminUsername" placeholder="请输入手机号作为管理员账号" autocomplete="new-password" type="text" />
            </el-form-item>
            <el-form-item label="管理员密码" prop="adminPassword">
              <el-input v-model="installForm.adminPassword" placeholder="请输入不少于6位的管理员密码" autocomplete="new-password" type="text" />
            </el-form-item>

            <!-- Server Info -->
            <el-divider content-position="left" style="width:90%">安装环境</el-divider>
            <el-form-item label="选择安装环境" prop="envType">
              <el-radio-group v-model="installForm.envType">
                <el-radio label="Kubernetes" disabled="disabled">Kubernetes环境</el-radio>
                <el-radio label="DockerCompose">Docker Compose环境</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="服务器地址" prop="serverIp">
              <el-input v-model="installForm.serverIp" placeholder="请输入安装服务器的IP地址" autocomplete="off" />
            </el-form-item>
            <el-form-item label="访问端口" prop="accessPort">
              <el-input v-model="installForm.accessPort" placeholder="请输入数字格式的访问端口" autocomplete="off" disabled="disabled" />
            </el-form-item>

            <el-form-item label="保存配置文件">
                <el-switch v-model="installForm.saveConfig" />
            </el-form-item>

            <el-form-item label="前后端加密">
                <el-switch v-model="installForm.isEncrypt" />
            </el-form-item>

            <!-- Submit and Reset Buttons -->
            <el-form-item style="margin-top: 40px">
              <el-button type="primary" @click="checkEnvironment()">检查环境</el-button>
              <el-button type="primary" :disabled="!isEnvValid" @click="submitForm(installFormRef)">生成安装配置</el-button>
              <el-button @click="resetForm(installFormRef)">重置</el-button>
            </el-form-item>

          </el-form>
        </el-col>

        <!-- Business Function Model Column -->
        <el-col :span="14">
          <BusinessFunctionModel />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage , ElLoading } from 'element-plus';
import BusinessFunctionModel from './businessPlugins.vue';

import { install , checkEnv } from '@/api/install';

const labelPosition = ref('right');
const loading = ref(false);
const installFormRef = ref(null);
const isEnvValid = ref(false); // Environment validation flag

// Define the form data
const installForm = reactive({
  version: '1.1.0-SNAPSHOT', // Default version
  apiKey: '',
  envType: 'DockerCompose', // Default value
  adminUsername: '',
  adminPassword: '',
  serverIp: '',
  accessPort: '30109',
  saveConfig: true , // Flag to indicate whether to save the configuration
});

// Version options configuration
const versionOptions = [
  {
    value: '1.1.0-SNAPSHOT',
    label: '1.1.0-SNAPSHOT（预览版）',
    description: '新功能的体验和预览，会有功能不完善的情况',
    disabled: false // This version is not selectable
  },
  {
    value: '1.1.0-GA',
    label: '1.1.0-GA（推广版）',
    description: '基于预览版问题的修复，功能较为完善',
    disabled: true // This version is selectable
  },
];

// Define validation rules
const rules = reactive({
  version: [{ required: true, message: '请选择一个版本', trigger: 'change' }],
  apiKey: [{ required: true, message: '请输入通义千问的API Key', trigger: 'blur' }],
  envType: [{ required: true, message: '请选择安装环境', trigger: 'change' }],
  adminUsername: [
    { required: true, message: '请输入手机号作为管理员账号', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ],
  adminPassword: [
    { required: true, message: '请输入不少于6位的管理员密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  serverIp: [{ required: true, message: '请输入安装服务器的IP地址', trigger: 'blur' }],
  accessPort: [
    { required: true, message: '请输入访问端口', trigger: 'blur' },
    { pattern: /^\d+$/, message: '端口必须为数字', trigger: 'blur' }
  ]
});

// Method to submit the form
const submitForm = (formEl) => {
  if (!formEl || !isEnvValid.value) return;
  formEl.validate((valid) => {
    if (valid && isEnvValid.value) {

      const loading = ElLoading.service({
        lock: true,
        text: '安装过程中，请勿关闭或者刷新浏览器',
        background: 'rgba(0, 0, 0, 0.5)',
      })

      // 直接使用 installForm 对象的数据来构建请求体
      const formData = {
        version: installForm.version,
        apiKey: installForm.apiKey,
        adminUsername: installForm.adminUsername,
        adminPassword: installForm.adminPassword,
        envType: installForm.envType,
        serverIp: installForm.serverIp,
        accessPort: installForm.accessPort,
        saveConfig: installForm.saveConfig,
        isEncrypt: installForm.isEncrypt
      };

      install(formData).then(res => {
        if(res.code === 200){
          ElMessage.success('安装成功');
        }else{
          ElMessage.error(res.msg);
        }
        loading.close();
      }).catch(err => {
        ElMessage.error('安装失败');
        loading.close();
      });
    } else {
      console.log('error submit!!');
      ElMessage.error('请检查并填写所有必填项，并确保环境检查已通过');
      return false;
    }
  });
};

// Method to reset the form
const resetForm = (formEl) => {
  if (!formEl) return;
  formEl.resetFields();
  isEnvValid.value = false; // Reset environment check status
};

// Method to check the environment
const checkEnvironment = () => {
  const isValid = true
  if (isValid) {

    const loading = ElLoading.service({
      lock: true,
      text: 'Loading',
      background: 'rgba(0, 0, 0, 0.5)',
    })

    checkEnv(installForm.envType).then( res => {
      console.log(res)
      isEnvValid.value = true;
      ElMessage.success('环境检查通过！');
      loading.close();
    }).catch(err => {
      console.log(err)
      isEnvValid.value = false ;
      ElMessage.error('环境检查失败！');
      loading.close();
    });
    
  } else {
    ElMessage.error('环境不符合要求，请检查设置后再次尝试。');
  }
};
</script>

<style scoped lang="scss">
.form-container {
  max-width: 100%;
  margin-left: auto;
  margin-right: auto;
  margin-top: 5px;
}

.acp-compoent-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}
</style>