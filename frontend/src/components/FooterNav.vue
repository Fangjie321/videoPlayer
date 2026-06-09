<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-links">
        <a href="#">关于我们</a>
        <a href="#">联系我们</a>
        <a href="#">用户协议</a>
        <a href="#">隐私政策</a>
        <a href="#">帮助中心</a>
      </div>
      <div class="copyright">© 2024 视频网站 - 类似B站的前后端分离视频平台</div>
    </div>
  </footer>
</template>

<script setup></script>

<style lang="scss" scoped>
.footer {
  background: #fff;
  border-top: 1px solid #e3e5e7;
  padding: 20px 0;
  margin-top: auto;
  .footer-content {
    width: 1200px;
    margin: 0 auto;
    text-align: center;
    .footer-links {
      display: flex;
      justify-content: center;
      gap: 30px;
      margin-bottom: 16px;
      a { color: #9499a0; font-size: 14px; transition: color 0.3s; &:hover { color: #00a1d6; } }
    }
    .copyright { color: #9499a0; font-size: 12px; }
  }
}
</style>