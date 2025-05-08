<script setup lang="ts">
import lupa from "../assets/sidebar/lupa.svg";
import {ref} from "vue";

const searchArticle = ref('')
const hoverBtn = ref<string | null>(null)
</script>

<template>
  <main class="article-container">
    <header id="search-login">

      <q-input class="col" v-model="searchArticle" label="Search" label-color="white" input-class="text-white" standout="bg-grey-10 text-white">

        <!-- Ícone de lupa no início -->
        <template v-slot:prepend>
          <q-img :src="lupa" style="width: 25px; height: 25px; margin: 12px" @click="searchArticle = ''" class="cursor-pointer" contain/>
        </template>
      </q-input>

      <div class="col-auto" style="margin-left: 25px">
        <q-btn
            :size="hoverBtn === null || hoverBtn === 'signIn' ? '20px' : '16px'"
            :color="hoverBtn === null || hoverBtn === 'signIn' ? 'yellow-14' : 'grey-10'"
            :text-color="hoverBtn === null || hoverBtn === 'signIn' ? 'black' : 'yellow-14'"
            :style="hoverBtn=== null || hoverBtn === 'signIn' ? 'border: solid 1px white' : 'border: none'"
            label="Sign In"
            no-caps
            @mouseenter="hoverBtn = 'signIn'"
            @mouseleave="hoverBtn = null"
        />
        <q-btn
            :size="hoverBtn === 'signUp' ? '20px' : '16px'"
            :color="hoverBtn === 'signUp' ? 'yellow-14' : 'grey-10'"
            :text-color="hoverBtn === 'signUp' ? 'black' : 'yellow-14'"
            :style="hoverBtn === 'signUp' ? 'border: solid 1px white' : 'border: none'"
            label="Sign Up"
            no-caps
            @mouseenter="hoverBtn = 'signUp'"
            @mouseleave="hoverBtn = null"
        />
      </div>

    </header>

    <q-linear-progress rounded size="1px" query track-color="grey-10" color="grey-8" class="q-mt-sm" style="margin-top: 20px"/>

    <section>
      <q-card class="my-card">
        <q-card-section horizontal>
          <q-img
              class="col"
              src="https://cdn.quasar.dev/img/parallax2.jpg"
          />

          <q-card-actions vertical class="justify-around">
            <q-btn flat round color="red" icon="favorite" />
            <q-btn flat round color="accent" icon="bookmark" />
            <q-btn flat round color="primary" icon="share" />
          </q-card-actions>
        </q-card-section>
      </q-card>
      <q-card class="my-card">
        <q-card-section horizontal>
          <q-img
              class="col"
              src="https://cdn.quasar.dev/img/parallax2.jpg"
          />

          <q-card-actions vertical class="justify-around">
            <q-btn flat round color="red" icon="favorite" />
            <q-btn flat round color="accent" icon="bookmark" />
            <q-btn flat round color="primary" icon="share" />
          </q-card-actions>
        </q-card-section>
      </q-card>
      <q-card class="my-card">
        <q-card-section horizontal>
          <q-img
              class="col"
              src="https://cdn.quasar.dev/img/parallax2.jpg"
          />

          <q-card-actions vertical class="justify-around">
            <q-btn flat round color="red" icon="favorite" />
            <q-btn flat round color="accent" icon="bookmark" />
            <q-btn flat round color="primary" icon="share" />
          </q-card-actions>
        </q-card-section>
      </q-card>
      <q-card class="my-card">
        <q-card-section horizontal>
          <q-img
              class="col"
              src="https://cdn.quasar.dev/img/parallax2.jpg"
          />

          <q-card-actions vertical class="justify-around">
            <q-btn flat round color="red" icon="favorite" />
            <q-btn flat round color="accent" icon="bookmark" />
            <q-btn flat round color="primary" icon="share" />
          </q-card-actions>
        </q-card-section>
      </q-card>

    </section>

  </main>
</template>

<style scoped lang="sass">
.article-container
  width: 99%
  padding: 10px
  margin: 5px

  header
    display: flex
    flex-direction: row
    justify-content: space-between
    flex-wrap: nowrap

  section
    display: flex
    flex-direction: row
    flex-wrap: wrap
    justify-content: space-between
    height: 70vh
    margin-top: 20px
    background-color: #292929

  .my-card
    width: 100%
    max-width: 350px
    max-height: 200px

.q-btn
  transition: all 0.5s ease
</style>