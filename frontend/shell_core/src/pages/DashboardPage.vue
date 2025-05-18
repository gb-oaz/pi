<script setup lang="ts">
// Lógica específica do dashboard aqui
import { onMounted } from "vue";
import { useAuthStore } from "../stores/authStore";
import router from "../routes.ts";

const authStore = useAuthStore();

onMounted(() => {
  if (!authStore.hasAnyRole(['TEACHER'])) {
    router.push({ name: 'home' })
  }
})
</script>

<template>
  <main class="dashboard-page">
    <h1>Profile Page</h1>
    <!-- Conteúdo da página de dashboard -->
  </main>
</template>

<style scoped lang="sass">
.dashboard-page
  padding: 20px
  color: white
</style>