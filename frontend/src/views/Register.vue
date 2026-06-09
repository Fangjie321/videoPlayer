<template>
  <div class="register-page">
    <div class="register-card">
      <h2>注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" placeholder="昵称（可选）" prefix-icon="UserFilled" size="large" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleRegister" class="register-btn">注册</el-button>
        </el-form-item>
      </el-form>
      <div class="footer"><span>已有账号？</span><router-link to="/login">立即登录</router-link></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '', confirmPassword: '', nickname: '' })
const validateConfirmPassword = (rule, value, callback) => { callback(value !== form.password ? new Error('两次输入的密码不一致') : undefined) }
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirmPassword, trigger: 'blur' }]
}

const handleRegister = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userApi.register({ username: form.username, password: form.password, nickname: form.nickname || form.username })
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (e) { console.error('注册失败', e) } finally { loading.value = false }
    }
  })
}
</script>

<style lang="scss" scoped>
.register-page {
  min-height: calc(100vh - 140px);
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  .register-card {
    width: 400px; background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    h2 { text-align: center; margin-bottom: 30px; color: #18191c; font-size: 28px; }
    .register-btn { width: 100%; }
    .footer { text-align: center; margin-top: 20px; color: #9499a0; font-size: 14px;
      a { color: #00a1d6; margin-left: 4px; &:hover { text-decoration: underline; } }
    }
  }
}
</style>