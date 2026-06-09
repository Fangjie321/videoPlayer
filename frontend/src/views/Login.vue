<template>
  <div class="login-page">
    <div class="login-card">
      <h2>登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleLogin" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="footer"><span>还没有账号？</span><router-link to="/register">立即注册</router-link></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userApi.login(form)
        userStore.login(res.data.token, res.data.user)
        ElMessage.success('登录成功')
        router.push('/')
      } catch (e) { console.error('登录失败', e) } finally { loading.value = false }
    }
  })
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: calc(100vh - 140px);
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  .login-card {
    width: 400px; background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    h2 { text-align: center; margin-bottom: 30px; color: #18191c; font-size: 28px; }
    .login-btn { width: 100%; }
    .footer { text-align: center; margin-top: 20px; color: #9499a0; font-size: 14px;
      a { color: #00a1d6; margin-left: 4px; &:hover { text-decoration: underline; } }
    }
  }
}
</style>