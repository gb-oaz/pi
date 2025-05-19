<script setup lang="ts">
// import { defineAsyncComponent} from "vue";
// const SidebarB = defineAsyncComponent(() => import((`remote-common/SidebarB`)));

</script>

<template>
<!--    <SidebarB msg="Shell Core" />-->
  <router-view></router-view>

</template>

<style lang="sass" scoped>
html, body
  margin: 0
  padding: 0
  width: 100%
  height: 100%
  background-color: #3f3f3f

</style>
