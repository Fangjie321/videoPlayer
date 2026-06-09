<template>
  <div class="video-page" v-loading="loading">
    <div class="video-main">
      <div class="player-container"><div ref="playerRef" class="player"></div></div>
      <div class="video-info" v-if="video">
        <h1 class="title">{{ video.title }}</h1>
        <div class="stats">
          <span><el-icon><View /></el-icon> {{ formatCount(video.viewCount) }} 播放</span>
          <span><el-icon><ChatDotRound /></el-icon> {{ formatCount(video.commentCount) }} 评论</span>
          <span>{{ formatDate(video.createTime) }}</span>
        </div>
        <div class="action-buttons">
          <el-button :type="isLiked ? 'primary' : 'default'" @click="handleLike"><el-icon><Star /></el-icon>{{ isLiked ? '已点赞' : '点赞' }} {{ formatCount(video.likeCount) }}</el-button>
          <el-button :type="isCollected ? 'primary' : 'default'" @click="handleCollect"><el-icon><FolderAdd /></el-icon>{{ isCollected ? '已收藏' : '收藏' }} {{ formatCount(video.collectCount) }}</el-button>
          <el-button><el-icon><Share /></el-icon>分享</el-button>
        </div>
        <div class="description">
          <div class="author">
            <el-avatar :size="48" :src="video.user?.avatar">{{ video.user?.nickname?.charAt(0) }}</el-avatar>
            <div class="author-info"><div class="name">{{ video.user?.nickname }}</div><div class="level">Lv{{ video.user?.level || 1 }}</div></div>
          </div>
          <div class="desc-text">{{ video.description || '暂无简介' }}</div>
        </div>
      </div>
      <div class="comment-section">
        <h3>评论 ({{ video?.commentCount || 0 }})</h3>
        <div class="comment-input" v-if="userStore.isLoggedIn">
          <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="发表你的看法..." />
          <el-button type="primary" @click="submitComment" :disabled="!commentContent.trim()">发表评论</el-button>
        </div>
        <div class="login-tip" v-else><el-button type="primary" @click="$router.push('/login')">登录后发表评论</el-button></div>
        <div class="comment-list">
          <div class="comment-item" v-for="comment in comments" :key="comment.id">
            <el-avatar :size="40" :src="comment.user?.avatar">{{ comment.user?.nickname?.charAt(0) }}</el-avatar>
            <div class="comment-content">
              <div class="comment-header"><span class="username">{{ comment.user?.nickname }}</span><span class="time">{{ formatDate(comment.createTime) }}</span></div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions"><span><el-icon><Star /></el-icon> {{ comment.likeCount }}</span></div>
            </div>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发吧！" />
        </div>
      </div>
    </div>
    <div class="video-sidebar">
      <h3>热门推荐</h3>
      <div class="recommend-list">
        <div class="recommend-item" v-for="item in recommendVideos" :key="item.id" @click="goToVideo(item.id)">
          <img :src="item.coverUrl" :alt="item.title" />
          <div class="info"><div class="title">{{ item.title }}</div><div class="meta">{{ formatCount(item.viewCount) }} 播放</div></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { videoApi, commentApi, actionApi, danmakuApi } from '@/api'
import { ElMessage } from 'element-plus'
import Player from 'xgplayer'
import 'xgplayer/dist/index.min.css'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const video = ref(null)
const isLiked = ref(false)
const isCollected = ref(false)
const comments = ref([])
const recommendVideos = ref([])
const commentContent = ref('')
const playerRef = ref(null)
let player = null

const videoId = () => Number(route.params.id)

const fetchVideoDetail = async () => {
  loading.value = true
  try {
    const res = await videoApi.getDetail(videoId())
    video.value = res.data.video
    isLiked.value = res.data.isLiked
    isCollected.value = res.data.isCollected
    initPlayer()
    fetchDanmaku()
    fetchComments()
  } catch (e) {
    ElMessage.error('视频不存在')
    router.push('/')
  } finally {
    loading.value = false
  }
}

const initPlayer = () => {
  if (!video.value) return
  player = new Player({
    el: playerRef.value,
    url: video.value.videoUrl,
    poster: video.value.coverUrl,
    width: '100%',
    height: '100%',
    controls: true,
    preload: 'auto',
    autoplay: false,
    playsinline: true,
    lang: 'zh-cn'
  })
}

const fetchDanmaku = async () => {
  try {
    const res = await danmakuApi.getList(videoId())
    const danmakuData = res.data.map(d => ({
      duration: 5000,
      id: d.id,
      start: Number(d.videoTime) * 1000,
      txt: d.content,
      style: { color: d.color || '#fff' }
    }))
    if (player && player.danmu) player.danmu.loadData(danmakuData)
  } catch (e) { console.error('获取弹幕失败', e) }
}

const fetchComments = async () => {
  try {
    const res = await commentApi.getList(videoId())
    comments.value = res.data
  } catch (e) { console.error('获取评论失败', e) }
}

const handleLike = async () => {
  try {
    const res = await actionApi.like(videoId())
    isLiked.value = res.data.isLiked
    if (isLiked.value) video.value.likeCount++
    else video.value.likeCount--
    ElMessage.success(res.data.message)
  } catch (e) { console.error('点赞失败', e) }
}

const handleCollect = async () => {
  try {
    const res = await actionApi.collect(videoId())
    isCollected.value = res.data.isCollected
    if (isCollected.value) video.value.collectCount++
    else video.value.collectCount--
    ElMessage.success(res.data.message)
  } catch (e) { console.error('收藏失败', e) }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return
  try {
    await commentApi.add({ videoId: videoId(), content: commentContent.value })
    ElMessage.success('评论成功')
    commentContent.value = ''
    fetchComments()
    video.value.commentCount++
  } catch (e) { console.error('发表评论失败', e) }
}

const goToVideo = (id) => { router.push(`/video/${id}`) }
const formatCount = (count) => {
  if (!count) return '0'
  return count >= 10000 ? (count / 10000).toFixed(1) + '万' : String(count)
}
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

onMounted(() => { fetchVideoDetail() })
onUnmounted(() => { if (player) player.destroy() })
</script>

<style lang="scss" scoped>
.video-page {
  display: flex;
  gap: 24px;
  .video-main { flex: 1; }
  .video-sidebar { width: 320px; h3 { margin-bottom: 16px; font-size: 16px; color: #18191c; }
    .recommend-list {
      .recommend-item {
        display: flex;
        gap: 12px;
        margin-bottom: 12px;
        cursor: pointer;
        img { width: 120px; height: 68px; object-fit: cover; border-radius: 4px; }
        .info { flex: 1;
          .title { font-size: 14px; color: #18191c; line-height: 1.4; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
          .meta { font-size: 12px; color: #9499a0; margin-top: 4px; }
        }
        &:hover { background: #f5f5f5; }
      }
    }
  }
  .player-container { background: #000; border-radius: 8px; overflow: hidden; aspect-ratio: 16/9; margin-bottom: 20px; }
  .player { width: 100%; height: 100%; }
  .video-info {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    .title { font-size: 20px; font-weight: 500; color: #18191c; margin-bottom: 12px; }
    .stats { display: flex; gap: 24px; color: #9499a0; font-size: 14px; margin-bottom: 16px; }
    .action-buttons { display: flex; gap: 12px; margin-bottom: 20px; }
    .description {
      .author { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid #eee;
        .author-info { .name { font-size: 16px; } .level { color: #9499a0; font-size: 12px; } }
      }
    }
  }
  .comment-section {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    h3 { font-size: 16px; margin-bottom: 16px; }
    .comment-input { margin-bottom: 20px; .el-button { margin-top: 12px; } }
    .comment-list {
      .comment-item { display: flex; gap: 12px; margin-bottom: 20px;
        .comment-content { flex: 1;
          .comment-header { display: flex; gap: 12px; margin-bottom: 8px;
            .username { font-weight: 500; color: #18191c; }
            .time { color: #9499a0; font-size: 12px; }
          }
          .comment-text { color: #333; margin-bottom: 8px; }
          .comment-actions { color: #9499a0; font-size: 12px; }
        }
      }
    }
  }
}
</style>
