<template>
  <div class="video-card" @click="goToVideo">
    <div class="cover">
      <img :src="video.coverUrl || defaultCover" :alt="video.title" />
      <div class="play-icon"></div>
      <span class="duration">{{ formatDuration(video.duration) }}</span>
    </div>
    <div class="info">
      <h3 class="title" :title="video.title">{{ video.title }}</h3>
      <div class="meta">
        <div class="author">
          <img :src="video.user?.avatar || defaultAvatar" :alt="video.user?.nickname" />
          <span>{{ video.user?.nickname || '未知用户' }}</span>
        </div>
        <div class="stats">
          <span><el-icon><View /></el-icon>{{ formatCount(video.viewCount) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({ video: { type: Object, required: true } })
const router = useRouter()
const defaultCover = 'https://picsum.photos/320/180'
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

const goToVideo = () => { router.push(`/video/${props.video.id}`) }

const formatDuration = (seconds) => {
  if (!seconds) return '00:00'
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

const formatCount = (count) => {
  if (!count) return '0'
  if (count >= 10000) return (count / 10000).toFixed(1) + '万'
  return count.toString()
}
</script>

<style lang="scss" scoped>
.video-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
    .cover .play-icon { opacity: 1; }
  }
  .cover {
    position: relative;
    width: 100%;
    padding-top: 56.25%;
    overflow: hidden;
    img { position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover; }
    .play-icon {
      position: absolute; top: 50%; left: 50%;
      transform: translate(-50%, -50%);
      width: 50px; height: 50px;
      background: rgba(0, 0, 0, 0.6);
      border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      opacity: 0; transition: opacity 0.3s ease;
      &::after {
        content: ''; width: 0; height: 0;
        border-left: 16px solid white;
        border-top: 10px solid transparent;
        border-bottom: 10px solid transparent;
        margin-left: 4px;
      }
    }
    .duration { position: absolute; bottom: 8px; right: 8px; background: rgba(0, 0, 0, 0.7); color: white; padding: 2px 6px; border-radius: 4px; font-size: 12px; }
  }
  .info { padding: 12px; .title { font-size: 14px; color: #18191c; line-height: 1.4; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; margin-bottom: 8px; } }
  .meta { display: flex; align-items: center; justify-content: space-between; color: #9499a0; font-size: 12px; .author { display: flex; align-items: center; gap: 6px; img { width: 24px; height: 24px; border-radius: 50%; } } .stats { display: flex; align-items: center; gap: 10px; span { display: flex; align-items: center; gap: 4px; } } }
}
</style>