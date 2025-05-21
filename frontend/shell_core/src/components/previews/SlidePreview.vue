<script setup lang="ts">
import { defineProps } from 'vue'

const props = defineProps({
  type: {
    type: String,
    required: true
  },
  data: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <div class="slide-preview-container">
    <!-- SLIDE_TITLE_1 Preview -->
    <div v-if="props.type === 'SLIDE_TITLE_1'" class="slide-preview slide-title-1">
      <h1 class="slide-title">{{ props.data.contentTitle || 'Your Title Here' }}</h1>
    </div>

    <!-- SLIDE_TITLE_2 Preview -->
    <div v-else-if="props.type === 'SLIDE_TITLE_2'" class="slide-preview slide-title-2">
      <h1 class="slide-title">{{ props.data.contentTitle || 'Your Title Here' }}</h1>
      <p class="slide-description">{{ props.data.contentDescription || 'Your description will appear here' }}</p>
    </div>

    <!-- SLIDE_TEXT_1 Preview -->
    <div v-else-if="props.type === 'SLIDE_TEXT_1'" class="slide-preview slide-text-1">
      <h2 class="slide-header">{{ props.data.contentHeader || 'Header' }}</h2>
      <h3 v-if="props.data.contentSubHeaderOne" class="slide-subheader">{{ props.data.contentSubHeaderOne }}</h3>
      <p class="slide-text">{{ props.data.contentTextOne || 'Your main text content will appear here' }}</p>
    </div>

    <!-- SLIDE_TEXT_2 Preview -->
    <div v-else-if="props.type === 'SLIDE_TEXT_2'" class="slide-preview slide-text-2">
      <h2 class="slide-header">{{ props.data.contentHeader || 'Header' }}</h2>

      <div class="content-section">
        <h3 v-if="props.data.contentSubHeaderOne" class="slide-subheader">{{ props.data.contentSubHeaderOne }}</h3>
        <p class="slide-text">{{ props.data.contentTextOne || 'First section content' }}</p>
      </div>

      <div class="content-section">
        <h3 v-if="props.data.contentSubHeaderTwo" class="slide-subheader">{{ props.data.contentSubHeaderTwo }}</h3>
        <p class="slide-text">{{ props.data.contentTextTwo || 'Second section content' }}</p>
      </div>
    </div>

    <!-- SLIDE_TEXT_MEDIA_1 Preview -->
    <div v-else-if="props.type === 'SLIDE_TEXT_MEDIA_1'" class="slide-preview slide-text-media-1">
      <h2 class="slide-header">{{ props.data.contentHeader || 'Header' }}</h2>

      <div class="content-wrapper">
        <div class="text-content">
          <p class="slide-text">{{ props.data.contentTextOne || 'Your main text content will appear here' }}</p>
        </div>

        <div class="media-content" v-if="props.data.contentMediaOne">
          <q-img
              :src="props.data.contentMediaOne"
              style="max-height: 200px; max-width: 100%"
              class="media-image"
          />
        </div>
      </div>
    </div>

    <!-- SLIDE_TEXT_MEDIA_2 Preview -->
    <div v-else-if="props.type === 'SLIDE_TEXT_MEDIA_2'" class="slide-preview slide-text-media-2">
      <div class="content-section">
        <h3 v-if="props.data.contentSubHeaderOne" class="slide-subheader">
          {{ props.data.contentSubHeaderOne }}
        </h3>
        <p class="slide-text">
          {{ props.data.contentTextOne || 'First section content' }}
        </p>
        <div class="media-content" v-if="props.data.contentMediaOne">
          <q-img
              :src="props.data.contentMediaOne"
              style="max-height: 200px; max-width: 100%"
              class="media-image"
          />
        </div>
      </div>

      <div class="content-section">
        <h3 v-if="props.data.contentSubHeaderTwo" class="slide-subheader">
          {{ props.data.contentSubHeaderTwo }}
        </h3>
        <p class="slide-text">
          {{ props.data.contentTextTwo || 'Second section content' }}
        </p>
        <div class="media-content" v-if="props.data.contentMediaTwo">
          <q-img
              :src="props.data.contentMediaTwo"
              style="max-height: 200px; max-width: 100%"
              class="media-image"
          />
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped lang="sass">
.slide-preview-container
  margin-top: 20px
  border: 1px solid #444
  border-radius: 8px
  padding: 20px
  background-color: #333
  color: white

.slide-preview
  min-height: 200px
  display: flex
  flex-direction: column
  justify-content: center
  padding: 20px

.slide-title-1
  text-align: center
  .slide-title
    font-size: 2.5rem
    margin: 0

.slide-title-2
  text-align: center
  .slide-title
    font-size: 2.5rem
    margin: 0
  .slide-description
    font-size: 1.2rem
    opacity: 0.8
    margin-top: 1rem

.slide-text-1, .slide-text-2
  .slide-header
    font-size: 2rem
    margin-bottom: 1.5rem
    color: #ffeb3b
  .slide-subheader
    font-size: 1.3rem
    margin: 1rem 0
    color: #bdbdbd
  .slide-text
    font-size: 1.1rem
    line-height: 1.6

.slide-text-2
  .content-section
    margin-bottom: 2rem
    &:last-child
      margin-bottom: 0

.slide-text-media-1
  .slide-header
    font-size: 2rem
    margin-bottom: 1.5rem
    color: #ffeb3b

  .content-wrapper
    display: flex
    gap: 2rem
    margin-top: 1rem

    .text-content
      flex: 1
      .slide-text
        font-size: 1.1rem
        line-height: 1.6

    .media-content
      flex: 1
      display: flex
      justify-content: center
      align-items: center

      .media-image
        border-radius: 4px
        border: 1px solid #555

.slide-text-media-2
  .content-section
    margin-bottom: 2rem
    padding-bottom: 2rem
    border-bottom: 1px solid #444

    &:last-child
      margin-bottom: 0
      padding-bottom: 0
      border-bottom: none

    .slide-subheader
      font-size: 1.3rem
      margin: 1rem 0
      color: #bdbdbd

    .slide-text
      font-size: 1.1rem
      line-height: 1.6
      margin-bottom: 1rem

    .media-content
      margin-top: 1rem
      .media-image
        border-radius: 4px
        border: 1px solid #555

@media (max-width: 600px)
  .slide-text-media-1
    .content-wrapper
      flex-direction: column
      gap: 1rem
</style>