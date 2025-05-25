<script setup lang="ts">
import { defineProps } from 'vue'
import type { IQuizMultipleChoice } from '../../../services/quiz/types/quiz/IQuizMultipleChoice.ts'

const props = defineProps<{ quiz: IQuizMultipleChoice }>()
</script>

<template>
  <div class="mc-teacher-preview-container">
    <h3 class="mc-question yellow-question">{{ props.quiz.contentQuestion }}</h3>
    <div class="mc-alternatives mc-alternatives-row">
      <div
        v-for="(alt, idx) in props.quiz.alternatives"
        :key="idx"
        class="mc-alternative-btn mc-jumbo-btn mc-disabled-btn"
      >
        <span class="mc-alternative-label">{{ alt }}</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
:root {
  --soft-grey: #f5f6fa;
  --soft-grey-text: #555;
  --soft-border: #e0e0e0;
}
.mc-teacher-preview-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 24px 0;
}
.yellow-question {
  color: #ffe066;
  font-size: 1.3rem;
  font-weight: 700;
  margin-bottom: 28px;
  text-align: center;
  letter-spacing: 0.01em;
}
.mc-alternatives {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: stretch;
  margin-bottom: 24px;
}
.mc-alternative-btn {
  transition: box-shadow 0.2s, background 0.2s;
  font-size: 1.1rem;
  font-weight: 500;
  border-radius: 16px;
  margin-bottom: 0;
  box-shadow: none;
  min-width: 160px;
  margin-right: 16px;
  padding: 0;
  display: flex;
  align-items: stretch;
  justify-content: center;
  background: var(--soft-grey) !important;
  color: var(--soft-grey-text) !important;
  border: 2px solid #e0e0e0;
}
.mc-jumbo-btn {
  height: 120px;
  min-width: 180px;
  max-width: 100%;
  font-size: 1.25rem;
  word-break: break-word;
  white-space: normal;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.mc-disabled-btn {
  opacity: 0.7;
  cursor: default;
  user-select: none;
  pointer-events: none;
}
.mc-alternative-label {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
  width: 100%;
  text-align: center;
  font-size: 1.2em;
  line-height: 1.3;
  word-break: break-word;
}
@media (max-width: 800px) {
  .mc-alternatives {
    flex-direction: column;
    align-items: stretch;
  }
  .mc-alternative-btn,
  .mc-jumbo-btn {
    min-width: 100%;
    margin-right: 0;
    margin-bottom: 16px;
  }
}
</style>
