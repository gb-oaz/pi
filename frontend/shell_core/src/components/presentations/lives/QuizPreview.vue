<script setup lang="ts">
import {computed, defineProps} from 'vue'
import QRadio from "quasar";
import QInput from "quasar";
import MultipleChoicePreview from './MultipleChoicePreview.vue';
import MultipleChoiceTeacherPreview from './MultipleChoiceTeacherPreview.vue';
import { liveStore } from '../../../stores/liveStore.ts';
import { useAuthStore } from '../../../stores/authStore.ts';

const authStore = useAuthStore()

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

const currentLive = computed(() => liveStore.getLive())
const scope = computed(() => authStore.scope)

const isOwner = computed(() => {
  const teacher = currentLive.value?.teacher
  if (!teacher || !scope.value) return false
  return (teacher.login === scope.value.login && teacher.code === scope.value.code)
})
</script>

<template>
  <div class="quiz-preview-container">

    <!-- QUIZ_MULTIPLE_CHOICE Preview -->
    <div v-if="props.type === 'QUIZ_MULTIPLE_CHOICE'">
      <MultipleChoiceTeacherPreview v-if="isOwner" :quiz="props.data" />
      <MultipleChoicePreview 
        v-else 
        :data="props.data" 
        :position="currentLive?.teacher?.control?.currentPosition"
      />
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
</style>