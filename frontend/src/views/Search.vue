<template>
  <div class="search-page">
    <div class="search-header"><h2>搜索结果</h2><p>关键词："{{ keyword }}"，共找到 {{ total }} 个视频</p></div>
    <div class="video-grid" v-loading="loading"><VideoCard v-for="video in videos" :key="video.id" :video="video" /></div>
    <el-empty v-if="!loading && videos.length === 0" description="没有找到相关视频" />
    <div class="pagination" v-if="total > pageSize">
      <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { videoApi } from '@/api'
import VideoCard from '@/components/VideoCard.vue'

const route = useRoute()
const videos = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = computed(() => route.query.keyword || '')
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

const searchVideos = async () => {
  if (!keyword.value) return
  loading.value = true
  try {
    const res = await videoApi.getList({ page: currentPage.value, size: pageSize.value, keyword: keyword.value })
    videos.value = res.data.records
    total.value = res.data.total
  } catch (e) { console.error('搜索失败', e) } finally { loading.value = false }
}
const changePage = (page) => { currentPage.value = page; searchVideos(); window.scrollTo({ top: 0, behavior: 'smooth' }) }

onMounted(() => { searchVideos() })
watch(() => route.query.keyword, () => { currentPage.value = 1; searchVideos() })
</script>

<style lang="scss" scoped>
.search-page {
  .search-header { margin-bottom: 24px; h2 { font-size: 24px; font-weight: 500; color: #18191c; margin-bottom: 8px; } p { color: #9499a0; font-size: 14px; } }
  .video-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; }
  .pagination { display: flex; justify-content: center; align-items: center; gap: 16px; margin-top: 30px;
    .page-btn { padding: 8px 20px; border: 1px solid #e3e5e7; background: white; border-radius: 4px; cursor: pointer; transition: all 0.3s ease;
      &:hover:not(:disabled) { border-color: #00a1d6; color: #00a1d6; }
      &:disabled { cursor: not-allowed; opacity: 0.5; }
    }
    .page-info { color: #61666d; font-size: 14px; }
  }
}
</style>