<template>
  <div class="home-page">
    <div class="category-nav">
      <div class="category-item" :class="{ active: currentCategory === null }" @click="selectCategory(null)">全部</div>
      <div v-for="cat in categories" :key="cat.id" class="category-item" :class="{ active: currentCategory === cat.id }" @click="selectCategory(cat.id)">{{ cat.name }}</div>
    </div>
    <div class="video-section">
      <div class="section-header">
        <h2>{{ currentCategoryName }}</h2>
        <span class="video-count">共 {{ total }} 个视频</span>
      </div>
      <div class="video-grid" v-loading="loading">
        <VideoCard v-for="video in videos" :key="video.id" :video="video" />
      </div>
      <el-empty v-if="!loading && videos.length === 0" description="暂无视频" />
      <div class="pagination" v-if="total > pageSize">
        <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
        <button v-for="page in displayPages" :key="page" class="page-btn" :class="{ active: currentPage === page }" @click="changePage(page)">{{ page }}</button>
        <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { videoApi, categoryApi } from '@/api'
import VideoCard from '@/components/VideoCard.vue'

const route = useRoute()
const videos = ref([])
const categories = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const currentCategory = ref(null)

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))
const currentCategoryName = computed(() => {
  if (!currentCategory.value) return '推荐视频'
  const cat = categories.value.find(c => c.id === currentCategory.value)
  return cat ? cat.name : '推荐视频'
})
const displayPages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, currentPage.value + 2)
  for (let i = start; i <= end; i++) pages.push(i)
  return pages
})

const fetchCategories = async () => {
  try { const res = await categoryApi.getList(); categories.value = res.data } catch (e) { console.error('获取分类失败', e) }
}
const fetchVideos = async () => {
  loading.value = true
  try {
    const res = await videoApi.getList({ page: currentPage.value, size: pageSize.value, categoryId: currentCategory.value })
    videos.value = res.data.records
    total.value = res.data.total
  } catch (e) { console.error('获取视频列表失败', e) } finally { loading.value = false }
}
const selectCategory = (categoryId) => { currentCategory.value = categoryId; currentPage.value = 1; fetchVideos() }
const changePage = (page) => { currentPage.value = page; fetchVideos(); window.scrollTo({ top: 0, behavior: 'smooth' }) }

onMounted(() => { fetchCategories(); fetchVideos() })
watch(() => route.query, () => { if (route.query.categoryId) { currentCategory.value = Number(route.query.categoryId) }; fetchVideos() })
</script>

<style lang="scss" scoped>
.home-page {
  .category-nav { display: flex; gap: 12px; margin-bottom: 24px; flex-wrap: wrap;
    .category-item { padding: 8px 20px; background: #fff; border-radius: 20px; font-size: 14px; cursor: pointer; transition: all 0.3s;
      &:hover { background: #e3e5e7; }
      &.active { background: #00a1d6; color: #fff; }
    }
  }
  .video-section {
    .section-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px;
      h2 { font-size: 20px; font-weight: 500; color: #18191c; }
      .video-count { font-size: 14px; color: #9499a0; }
    }
    .video-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; }
  }
}
</style>