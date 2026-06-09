<template>
  <div class="app-container">
    <HeaderNav />
    <main class="main-content">
      <router-view />
    </main>
    <FooterNav />
  </div>
</template>

<script setup>
import HeaderNav from '@/components/HeaderNav.vue'
import FooterNav from '@/components/FooterNav.vue'
</script>

<style lang="scss">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: "PingFang SC","Microsoft YaHei","Noto Sans SC","Source Han Sans SC", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  background-color: #f4f4f4;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

a {
  text-decoration: none;
  color: inherit;
}
</style>