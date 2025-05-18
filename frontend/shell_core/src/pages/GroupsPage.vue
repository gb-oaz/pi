<script setup lang="ts">
// Lógica específica do groups aqui
import { onMounted } from "vue";
import { useAuthStore } from "../stores/authStore";
import router from "../routes.ts";

const authStore = useAuthStore();

onMounted(() => {
  if (!authStore.hasAnyRole(['STUDENT', 'TEACHER'])) {
    router.push({ name: 'home' })
  }
})
</script>

<template>
  <main class="groups-page">
    <h1>Groups Page</h1>
    <!-- Conteúdo da página de Groups -->
  </main>
</template>

<style scoped lang="sass">
.groups-page
  padding: 20px
  color: white
</style>