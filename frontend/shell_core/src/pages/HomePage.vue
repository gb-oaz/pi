<script setup lang="ts">

import Sidebar from "../components/Sidebar.vue";
import Article from "../components/Article.vue";
</script>

<template>
  <main id="home-page">
    <section id="sidebar" class="col-auto">
      <Sidebar/>
    </section>
    <section id="articles" class="col">
      <Article/>
    </section>
  </main>
</template>

<style scoped lang="sass">
#home-page
  display: flex
  flex-direction: row
  flex-wrap: nowrap
  justify-content: space-between
  background-color: #3f3f3f
  height: 100vh

  #sidebar, #articles
    display: flex
    flex-direction: column
    justify-content: flex-start
    align-items: flex-start

  #sidebar
    background-color: #292929
    min-width: 300px

  #articles
    background-color: #292929

</style>