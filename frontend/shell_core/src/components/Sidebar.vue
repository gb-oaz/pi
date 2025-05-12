<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import logo from '../assets/by_gw-q.png'
import lupa from '../assets/sidebar/lupa.svg'
import sinal from '../assets/sidebar/sinal.svg'
import home from '../assets/sidebar/home.svg'

const searchLive = ref('')
const windowWidth = ref(window.innerWidth)

const updateWidth = () => {
  windowWidth.value = window.innerWidth
}

onMounted(() => {
  window.addEventListener('resize', updateWidth)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateWidth)
})

const isMobile = computed(() => windowWidth.value <= 720)
const showSidebar = ref(false)
</script>

<template>
  <!-- Versão desktop (sidebar normal) - só aparece acima de 720px -->
  <aside
      v-show="!isMobile"
      class="sidebar-container q-pa-sm column items-start justify-start full-height"
  >
    <!-- Conteúdo da sidebar... -->
    <q-img :src="logo" style="width: 45px; height: 45px" class="q-mb-md" contain />

    <q-input v-model="searchLive" color="yellow" bg-color="grey-14" label="Live" label-color="white" class="full-width" input-class="text-white" rounded standout bottom-slots counter>
      <template v-slot:prepend>
        <q-img :src="sinal" style="width: 15px; height: 11px; margin: 7px" contain/>
      </template>
      <template v-slot:append>
        <q-img :src="lupa" style="width: 20px; height: 20px; margin: 7px" @click="searchLive = ''" class="cursor-pointer" contain/>
      </template>
    </q-input>

    <div class="text-white text-caption q-mb-sm">Menu</div>

    <q-btn align="between" color="yellow-14" class="full-width" text-color="black" size="17px" label="Home" no-caps>
      <q-img :src="home" contain style="width: 17px; height: 17px"/>
    </q-btn>
  </aside>

  <!-- Versão mobile (apenas FAB) -->
  <q-fab
      v-if="isMobile"
      v-model="showSidebar"
      color="yellow-14"
      icon="menu"
      text-color="black"
      direction="up"
      vertical-actions-align="right"
      class="fab-button"
  />

  <!-- Dialog com o conteúdo da sidebar -->
  <q-dialog v-model="showSidebar">
    <q-card class="sidebar-dialog">
      <q-card-section class="column items-start justify-start">
        <q-img :src="logo" style="width: 45px; height: 45px" class="q-mb-md" contain />

        <q-input v-model="searchLive" color="yellow" bg-color="grey-14" label="Live" label-color="white" class="full-width" input-class="text-white" rounded standout bottom-slots counter>
          <template v-slot:prepend>
            <q-img :src="sinal" style="width: 15px; height: 11px; margin: 7px" contain/>
          </template>
          <template v-slot:append>
            <q-img :src="lupa" style="width: 20px; height: 20px; margin: 7px" @click="searchLive = ''" class="cursor-pointer" contain/>
          </template>
        </q-input>

        <div class="text-white text-caption q-mb-sm">Menu</div>

        <q-btn align="between" color="yellow-14" class="full-width" text-color="black" size="17px" label="Home" no-caps @click="showSidebar = false">
          <q-img :src="home" contain style="width: 17px; height: 17px"/>
        </q-btn>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<style scoped lang="sass">
.sidebar-container
  background-color: #242424
  width: 92%
  padding: 20px
  margin: 15px
  border-radius: 5px
  display: none

  &:not(.mobile)
    display: flex

.fab-button
  position: fixed
  bottom: 20px
  right: 20px
  z-index: 1000

.sidebar-dialog
  background-color: #242424
  width: 280px
  border-radius: 5px

body.mobile .sidebar-container
  display: none !important
</style>