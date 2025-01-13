<template>
  <nav class=" header-text">

    <div class="acp-header-item ">
      <router-link class="header-label-text" to="/index">
        <i class="fa-solid fa-screwdriver-wrench"></i> 官网 
      </router-link>
    </div>
    <div class="acp-header-item ">
      <a target="_blank" class="header-label-text" href="http://portal.infra.linesno.com/operation/02_环境部署/02_Docker部署.html">
        <i class="fa-regular fa-file-word"></i> 安装文档 
      </a>
    </div>

  </nav>
</template>
<script>
export default {
  name: "TopHeader",
  components: {},
  data() {
    return {
      drawer: false,
      direction: 'rtl',
    };
  },
  computed: {
    role: {
      get() {
        return this.$store.state.user.roles;
      },
    },
    account: {
      get() {
        // const { account } =  null ; // this.$store.state.user;
        // return account ? account : "";
        return "";
      },
    },
    nickname: {
      get() {
        const nickname = '超级管理员' //  this.$store.state.user.nickname;
        return nickname;
      },
    },
    name: {
      get() {
        const name = '超级管理员'; //  this.$store.state.user.name;
        return name;
      },
    },
    avator: {
      get() {
        const avatar = "http://data.linesno.com/switch_header.png"; //  this.$store.state.user.avatar;
        return avatar;
      },
    },
    setting: {
      get() {
        return this.$store.state.settings.showSettings;
      },
      set(val) {
        this.$store.dispatch("settings/changeSetting", {
          key: "showSettings",
          value: val,
        });
      },
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav;
      },
    },
  },
  methods: {
    lockScreen() {
      this.$message({
        message: '功能在内测试中',
        type: 'success'
      });
    },
    handleCommand(command) {
      this.$router.push({ name: command });
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
    submitForm() { },
    cancel() { },
    dashboardHome() {
      window.location.href = this.saasUrl;
    },
    // 表单重置
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: undefined,
        status: "0",
      };
      this.resetForm("form");
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工单";
    },
    async logout() {
      this.$confirm("确定注销并退出系统吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        // this.$store.dispatch('LogOut').then(res()=>{
        //     window.location.href="/"
        // })
        this.$store.dispatch("LogOut").then(() => {
          window.location.href = "/";
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped>

.color-text-secondary , .color-text-primary{
  font-size: var(--el-font-size-base);

  .copy-user-id {
    margin-left: 10px;
    cursor: pointer;
  }
}

</style>


