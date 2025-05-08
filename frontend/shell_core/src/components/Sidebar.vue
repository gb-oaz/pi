<script setup lang="ts">
import { ref } from 'vue'
import logo from '../assets/by_gw-q.png'
import lupa from '../assets/sidebar/lupa.svg'
import sinal from '../assets/sidebar/sinal.svg'
import home from '../assets/sidebar/home.svg'

const searchLive = ref('')
</script>

<template>
  <main class="sidebar-container q-pa-sm column items-start justify-start full-height">
    <!-- Logo -->
    <q-img :src="logo" style="width: 45px; height: 45px" class="q-mb-md" contain />

    <!-- Input de busca -->
    <q-input v-model="searchLive" color="yellow" bg-color="grey-14" label="Live" label-color="white" class="full-width" input-class="text-white" rounded standout bottom-slots counter>
      <!-- Ícone no início -->
      <template v-slot:prepend>
        <q-img :src="sinal" style="width: 15px; height: 11px; margin: 7px" contain/>
      </template>

      <!-- Ícone de lupa no final -->
      <template v-slot:append>
        <q-img :src="lupa" style="width: 20px; height: 20px; margin: 7px" @click="searchLive = ''" class="cursor-pointer" contain/>
      </template>
    </q-input>

    <!-- Menu label -->
    <div class="text-white text-caption q-mb-sm">Menu</div>

    <!-- Menu item -->
    <q-btn align="between" color="yellow-14" class="full-width" text-color="black" size="17px" label="Home" no-caps>
      <q-img :src="home" contain style="width: 17px; height: 17px"/>
    </q-btn>
  </main>
</template>

<style scoped lang="sass">
.sidebar-container
  background-color: #242424
  width: 92%
  padding: 20px
  margin: 15px
  border-radius: 5px
</style>