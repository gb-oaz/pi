<script setup lang="ts">
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
  <main class="profile-page">
    <h1>Profile Page</h1>
    <!-- Conteúdo da página de perfil -->
  </main>
</template>

<style scoped lang="sass">
.profile-page
  padding: 20px
  color: white
</style>