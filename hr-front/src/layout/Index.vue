<template>
  <div class="layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="sidebarCollapsed ? '64px' : '220px'" class="sidebar">
        <div class="logo">
          <span v-if="!sidebarCollapsed">人事管理系统</span>
          <span v-else>HR</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="sidebarCollapsed"
          :unique-opened="true"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="/employee">
            <i class="el-icon-user"></i>
            <span slot="title">员工管理</span>
          </el-menu-item>
          <!-- 部门管理：管理员和部门负责人可见 -->
          <el-menu-item v-if="isAdmin" index="/department">
            <i class="el-icon-s-order"></i>
            <span slot="title">部门管理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <i class="el-icon-s-custom"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
          <el-menu-item index="/role">
            <i class="el-icon-s-check"></i>
            <span slot="title">角色管理</span>
          </el-menu-item>
          <!-- 权限管理：管理员和部门负责人可见 -->
          <el-menu-item v-if="isAdmin" index="/permission">
            <i class="el-icon-s-flag"></i>
            <span slot="title">权限管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <i class="el-icon-s-fold" @click="toggleSidebar"></i>
          </div>
          <div class="header-right">
            <span class="username">{{ userInfo.username || '管理员' }}</span>
            <span class="role-tag" v-if="roleCodes.length > 0">
              {{ formatRoleCodes(roleCodes) }}
            </span>
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区域 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Index',
  data() {
    return {
      sidebarCollapsed: false,
      activeMenu: this.$route.path
    }
  },
  computed: {
    ...mapGetters(['userInfo', 'roleCodes', 'isAdmin'])
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确认退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout').then(() => {
            this.$router.push('/login')
          })
        })
      }
    },
    formatRoleCodes(codes) {
      const roleMap = {
        'ADMIN': '管理员',
        'HR_MANAGER': '部门负责人',
        'EMPLOYEE': '员工'
      }
      return codes.map(code => roleMap[code] || code).join('/')
    }
  }
}
</script>

<style scoped>
.layout {
  height: 100%;
}

.el-container {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  transition: width 0.28s;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b3a4b;
}

.el-menu {
  border-right: none;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
}

.header-left i {
  font-size: 20px;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
}

.username {
  margin-right: 10px;
  color: #606266;
}

.role-tag {
  margin-right: 15px;
  padding: 2px 8px;
  font-size: 12px;
  color: #409EFF;
  background-color: #ecf5ff;
  border-radius: 4px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #606266;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
