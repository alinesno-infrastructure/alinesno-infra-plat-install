<template>
    <div class="acp-component-container">
        <div class="acp-compoent-title">
            安装日志
            <span>
                <!-- <el-button type="primary" bg @click="startInstallation"><i class="fa-solid fa-pen-nib"></i> 安装AIP</el-button> -->
                <el-button type="warning" bg @click="toggleScrolling">{{ isAutoScroll ? '暂停滚动' : '恢复滚动' }}</el-button>
            </span>
        </div>
        <div class="console-log" ref="consoleLog" @scroll="onScroll">
            <!-- 日志内容将动态插入到这里 -->
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { ElMessage , ElLoading } from 'element-plus';

const baseUrl = import.meta.env.VITE_APP_BASE_API;
const consoleLog = ref(null);
let eventSource = null;
let loading = null ;
const logMessages = ref([]); // 用于存储日志消息的数组
const maxLogEntries = 100; // 设置最大日志条目数
const isAutoScroll = ref(true); // 控制是否自动滚动

// 模拟开始安装并启动SSE连接
const startInstallation = () => {
    if (eventSource) {
        eventSource.close();
    }

    eventSource = new EventSource(baseUrl + '/api/openConn/9527');

    eventSource.onmessage = function (event) {

        if (event.data === 'ping' || event.data === '[DONE]') {
            loading.close();
            return;
        }

        logMessages.value.push(event.data);

        if (logMessages.value.length > maxLogEntries) {
            logMessages.value.shift(); // 移除最老的日志条目
        }

        renderLogs();
    };

    eventSource.onopen = function () {
        console.log('EventSource opened');
    };

    eventSource.onerror = function (error) {
        console.error('EventSource failed:', error);
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log('连接已断开，请检查网络设置或稍后再试。');
        }
    };
};

// 渲染日志到DOM中
const renderLogs = async () => {
    consoleLog.value.innerHTML = '';
    logMessages.value.forEach(log => {

        // 解析JSON对象
        const logJson = JSON.parse(log);
        let level = logJson.level.toLowerCase(); // 将级别转换为小写，确保匹配CSS类名

        // 创建日志条目元素并设置文本内容
        const logMessage = document.createElement('div');
        logMessage.textContent = (logJson.timestamp + " " + logJson.body + " " + logJson.level + " " + logJson.className);

        // 添加通用的日志条目样式
        logMessage.classList.add('log-entry');

        // 根据日志级别添加特定的样式类
        if (['debug', 'info', 'warn', 'error'].includes(level)) {
            logMessage.classList.add(level);
        } else {
            logMessage.classList.add('info'); // 默认使用info样式
        }

        // 将日志条目添加到DOM中
        consoleLog.value.appendChild(logMessage);

    });

    await nextTick();
    if (isAutoScroll.value) {
        scrollToEnd();
    }
};

// 自动滚动到底部
const scrollToEnd = () => {
    if (consoleLog.value) {
        consoleLog.value.scrollTop = consoleLog.value.scrollHeight;
    }
};

// 切换日志滚动状态
const toggleScrolling = () => {
    isAutoScroll.value = !isAutoScroll.value;
};

// 监听日志容器滚动事件
const onScroll = () => {
    if (!isAutoScroll.value && consoleLog.value.scrollTop + consoleLog.value.clientHeight >= consoleLog.value.scrollHeight - 10) {
        isAutoScroll.value = true; // 当用户滚动到接近底部时自动恢复滚动
    }
};

// 确保在组件卸载时关闭EventSource
onUnmounted(() => {
    if (eventSource) {
        eventSource.close();
    }
});

onMounted(() => {

    nextTick(() => {
        loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
        })
        startInstallation();
    });
});
</script>

<style scoped lang="scss">
.acp-component-container {
    max-width: 100%;
    margin-left: auto;
    margin-right: auto;
    margin-top: 0px;
}

.acp-compoent-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.componet-add-plugins {
    cursor: pointer;
    color: #409eff;

    &:hover {
        text-decoration: underline;
    }
}

.console-log {
    background-color: #2d2d2d;
    color: #bfbfbf;
    padding: 10px;
    border-radius: 4px;
    height: calc(100vh - 280px);
    /* 固定高度 */
    overflow-x: auto;
    /* 允许水平滚动 */
    overflow-y: auto;
    /* 允许垂直滚动 */
    font-family: monospace;
    white-space: nowrap;
    /* 防止文本自动换行 */
}
</style>

<style lang="scss">
/* 样式表 */
.log-entry {
    margin-bottom: 5px;
    border-radius: 3px;
}

.debug {
    /* 浅蓝色背景 */
    color: #1790ff;
    /* 蓝色文字 */
}

.info {
    /* 浅绿色背景 */
    color: #52c41a;
    /* 绿色文字 */
}

.warn {
    /* 浅黄色背景 */
    color: #faad14;
    /* 橙色文字 */
}

.error {
    /* 浅红色背景 */
    color: #f5222d;
    /* 红色文字 */
}
</style>