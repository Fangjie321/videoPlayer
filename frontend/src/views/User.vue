<template>
  <div class="user-page">
    <div class="user-card">
      <div class="user-header">
        <div class="avatar-section">
          <el-avatar :size="100" :src="userInfo?.avatar">{{ userInfo?.nickname?.charAt(0) }}</el-avatar>
          <div class="user-info">
            <h2>{{ userInfo?.nickname }}</h2>
            <div class="meta"><span class="level">Lv{{ userInfo?.level || 1 }}</span><span class="coin"><el-icon><Coin /></el-icon>{{ userInfo?.coin || 0 }} 硬币</span></div>
            <p class="signature">{{ userInfo?.signature || '这个人很懒，什么都没写' }}</p>
          </div>
        </div>
        <el-button type="primary" @click="showEditDialog = true"><el-icon><Edit /></el-icon>编辑资料</el-button>
      </div>
    </div>
    <div class="user-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我的收藏" name="collect">
          <div class="video-grid" v-loading="loading"><VideoCard v-for="video in collectVideos" :key="video.id" :video="video" /></div>
          <el-empty v-if="!loading && collectVideos.length === 0" description="暂无收藏" />
        </el-tab-pane>
        <el-tab-pane label="账号设置" name="settings">
          <div class="settings-form">
            <el-form :model="editForm" label-width="80px">
              <el-form-item label="用户名"><el-input v-model="editForm.username" disabled /></el-form-item>
              <el-form-item label="昵称"><el-input v-model="editForm.nickname" /></el-form-item>
              <el-form-item label="邮箱"><el-input v-model="editForm.email" /></el-form-item>
              <el-form-item label="个性签名"><el-input v-model="editForm.signature" type="textarea" :rows="3" /></el-form-item>
              <el-form-item><el-button type="primary" @click="saveUserInfo">保存修改</el-button></el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog v-model="showEditDialog" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称"><el-input v-model="editForm.nickname" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="editForm.email" /></el-form-item>
        <el-form-item label="个性签名"><el-input v-model="editForm.signature" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="showEditDialog = false">取消</el-button><el-button type="primary" @click="saveUserInfo">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'
import VideoCard from '@/components/VideoCard.vue'

const userStore = useUserStore()
const userInfo = ref(null)
const activeTab = ref('collect')
const loading = ref(false)
const showEditDialog = ref(false)
const collectVideos = ref([])
const editForm = reactive({ username: '', nickname: '', email: '', signature: '' })

const fetchUserInfo = async () => {
  try {
    const res = await userApi.getUserInfo()
    userInfo.value = res.data
    editForm.username = res.data.username
    editForm.nickname = res.data.nickname
    editForm.email = res.data.email || ''
    editForm.signature = res.data.signature || ''
  } catch (e) { console.error('获取用户信息失败', e) }
}

const saveUserInfo = async () => {
  try {
    await userApi.updateUser(editForm)
    userStore.updateUserInfo(editForm)
    userInfo.value = { ...userInfo.value, ...editForm }
    showEditDialog.value = false
    ElMessage.success('保存成功')
  } catch (e) { console.error('保存失败', e) }
}

onMounted(() => { fetchUserInfo() })
</script>

<style lang="scss" scoped>
.user-page {
  .user-card { background: #fff; border-radius: 12px; padding: 30px; margin-bottom: 20px;
    .user-header { display: flex; justify-content: space-between; align-items: flex-start;
      .avatar-section { display: flex; gap: 24px;
        .user-info {
          h2 { font-size: 24px; margin-bottom: 12px; color: #18191c; }
          .meta { display: flex; align-items: center; gap: 16px; margin-bottom: 12px;
            .level { background: linear-gradient(135deg, #00a1d6, #00b5e2); color: #fff; padding: 2px 12px; border-radius: 12px; font-size: 12px; }
            .coin { display: flex; align-items: center; gap: 4px; color: #9499a0; font-size: 14px; }
          }
          .signature { color: #61666d; font-size: 14px; }
        }
      }
    }
  }
  .user-content { background: #fff; border-radius: 12px; padding: 20px;
    .video-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
  }
}
</style>