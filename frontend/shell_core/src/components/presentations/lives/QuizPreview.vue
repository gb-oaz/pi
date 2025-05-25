<script setup lang="ts">
import { defineProps } from 'vue'
import QRadio from "quasar";
import QInput from "quasar";

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
  <div class="quiz-preview-container">
    <!-- Common Question -->
    <h3 class="quiz-question">{{ props.data.contentQuestion || 'Your question will appear here' }}</h3>

    <!-- QUIZ_MULTIPLE_CHOICE Preview -->
    <div v-if="props.type === 'QUIZ_MULTIPLE_CHOICE'" class="quiz-options">
      <div v-for="(alt, index) in props.data.alternatives" :key="index" class="option">
        <q-radio :val="alt" :label="alt || `Option ${index + 1}`" />
      </div>
    </div>

    <!-- QUIZ_FILL_SPACE Preview -->
    <div v-else-if="props.type === 'QUIZ_FILL_SPACE'" class="fill-space-preview">
      <q-input outlined label="Type your answer here" />
    </div>

    <!-- QUIZ_OPEN Preview -->
    <div v-else-if="props.type === 'QUIZ_OPEN'" class="open-quiz-preview">
      <q-input
          outlined
          type="textarea"
          :label="`Answer (min ${props.data.quantityCharacters} chars)`"
      />
    </div>

    <!-- QUIZ_POLL Preview -->
    <div v-else-if="props.type === 'QUIZ_POLL'" class="poll-preview">
      <div v-for="(option, index) in props.data.answers" :key="index" class="poll-option">
        <q-radio :val="option" :label="option || `Option ${index + 1}`" />
      </div>
    </div>

    <!-- QUIZ_TRUE_FALSE Preview -->
    <div v-else-if="props.type === 'QUIZ_TRUE_FALSE'" class="true-false-preview">
      <div class="row q-gutter-sm">
        <q-radio val="true" label="True" />
        <q-radio val="false" label="False" />
      </div>
    </div>

    <!-- QUIZ_WORD_CLOUD Preview -->
    <div v-else-if="props.type === 'QUIZ_WORD_CLOUD'" class="word-cloud-preview">
      <q-input outlined label="Type your word(s) here" />
      <div class="word-cloud-example q-mt-md">
        <div class="text-caption">Word cloud visualization will appear here</div>
        <div v-if="props.data.answers.length" class="word-cloud-tags q-mt-sm">
          <q-chip v-for="(word, index) in props.data.answers" :key="index" color="yellow-8" text-color="black">
            {{ word }}
          </q-chip>
        </div>
      </div>
    </div>

    <!-- Timer and Reward Info -->
    <div class="quiz-meta q-mt-md">
      <div class="row items-center justify-between">
        <div class="timer">
          <q-icon name="timer" size="sm" />
          {{ props.data.timerSeconds || 30 }} seconds
        </div>
        <div class="reward">
          <q-icon name="military_tech" size="sm" />
          {{ props.data.reward || 50 }} points
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="sass">
.quiz-preview-container
  margin-top: 20px
  border: 1px solid #444
  border-radius: 8px
  padding: 20px
  background-color: #333
  color: white

.quiz-question
  font-size: 1.2rem
  margin-bottom: 1.5rem
  color: #ffeb3b

.quiz-options, .poll-preview
  display: flex
  flex-direction: column
  gap: 12px
  margin-bottom: 1rem

.option, .poll-option
  padding: 8px 12px
  border-radius: 4px
  background-color: #424242
  border-left: 4px solid #ffeb3b

.fill-space-preview, .open-quiz-preview, .word-cloud-preview
  margin-bottom: 1rem

.word-cloud-tags
  display: flex
  flex-wrap: wrap
  gap: 8px

.quiz-meta
  padding-top: 12px
  border-top: 1px solid #555
  color: #bdbdbd
  font-size: 0.9rem
  .row
    gap: 20px
</style>