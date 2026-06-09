<template>
  <header class="header">
    <div class="header-content">
      <div class="logo" @click="router.push('/')">
        <el-icon size="28"><VideoCamera /></el-icon>
        <span>视频网站</span>
      </div>
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索视频" @keyup.enter="handleSearch" clearable>
          <template #append>
            <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
          </template>
        </el-input>
      </div>
      <div class="nav-links">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/" class="nav-item">热门</router-link>
        <router-link to="/" class="nav-item">分类</router-link>
      </div>
      <div class="user-area">
        <template v-if="userStore.isLoggedIn">
          <el-dropdown trigger="click">
            <div class="user-info">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">{{ userStore.userInfo?.nickname?.charAt(0) }}</el-avatar>
              <span class="username">{{ userStore.userInfo?.nickname }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/user')"><el-icon><User /></el-icon>个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleLogout"><el-icon><SwitchButton /></el-icon>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
  }
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/')
}
</script>

<style lang="scss" scoped>
.header {
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  .header-content {
    width: 1200px;
    margin: 0 auto;
    height: 60px;
    display: flex;
    align-items: center;
    gap: 30px;
  }
  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: #00a1d6;
    font-size: 20px;
    font-weight: bold;
  }
  .search-box {
    flex: 1;
    max-width: 400px;
    :deep(.el-input-group__append) {
      background: #00a1d6;
      color: white;
      border-color: #00a1d6;
    }
  }
  .nav-links {
    display: flex;
    gap: 24px;
    .nav-item {
      color: #18191c;
      font-size: 14px;
      padding: 8px 0;
      border-bottom: 2px solid transparent;
      transition: all 0.3s;
      &:hover, &.router-link-active { color: #00a1d6; border-bottom-color: #00a1d6; }
    }
  }
  .user-area {
    display: flex;
    align-items: center;
    gap: 12px;
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      .username { font-size: 14px; color: #18191c; }
    }
  }
}
</style>