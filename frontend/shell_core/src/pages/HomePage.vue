<script setup lang="ts">
import Sidebar from "../components/sidebar/Sidebar.vue";

</script>

<template>
  <main class="home-page">
    <aside class="sidebar">
      <Sidebar />
    </aside>
    <section class="articles">
      <router-view></router-view>
    </section>
  </main>
</template>

<style scoped lang="sass">
.home-page
  display: flex
  background-color: #3f3f3f

  .sidebar,
  .articles
    display: flex
    flex-direction: column
    justify-content: flex-start
    align-items: flex-start
    background-color: #292929

  .sidebar
    height: 100vh
  .articles
    width: 99vw
</style>