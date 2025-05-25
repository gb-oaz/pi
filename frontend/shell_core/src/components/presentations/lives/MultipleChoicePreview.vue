<script setup lang="ts">
import { ref, computed, defineProps } from 'vue'
import type { IQuizMultipleChoice } from "../../../services/quiz/types/quiz/IQuizMultipleChoice.ts";
import { LiveApi } from "../../../services/live/LiveApi.ts";
import { liveStore } from "../../../stores/liveStore.ts";

const liveApi = new LiveApi()
const props = defineProps<{ data: IQuizMultipleChoice }>()

const currentLive = computed(() => liveStore.getLive())
const selected = ref<any[]>([])
const sending = ref(false)
const sent = ref(false)

function toggleSelect(alt: any) {
  if (sent.value) return
  const idx = selected.value.indexOf(alt)
  if (idx === -1) {
    selected.value.push(alt)
  } else {
    selected.value.splice(idx, 1)
  }
}

async function sendAnswers() {
  if (!selected.value.length) return
  const teacher = currentLive.value?.teacher
  if (!teacher) return
  const liveKey = `LIVE${teacher.login}CODE${teacher.code}`
  sending.value = true
  await liveApi.addAnswerPupil(liveKey, selected.value)
  sending.value = false
  sent.value = true
}
</script>

<template>
  <div class="mc-preview-container">
    <h3 class="mc-question yellow-question">{{ props.data.contentQuestion }}</h3>
    <div class="mc-alternatives mc-alternatives-row">
      <q-btn
        v-for="(alt, idx) in props.data.alternatives"
        :key="idx"
        :class="['mc-alternative-btn', 'mc-jumbo-btn', 'soft-btn', { 'selected-btn': selected.includes(alt) }]"
        @click="toggleSelect(alt)"
        :disable="sent"
        size="xl"
        outline
        :style="selected.includes(alt) ? 'background: #ffe066 !important; color: #111 !important; border: 2px solid #ffe066 !important;' : ''"
      >
        <span class="mc-alternative-label">{{ alt }}</span>
      </q-btn>
    </div>
    <q-btn
      label="Enviar resposta"
      class="mc-send-btn soft-send-btn"
      :disable="!selected.length || sending || sent"
      @click="sendAnswers"
      style="margin-top: 16px; width: 100%;"
      size="lg"
      color="soft-accent"
      text-color="soft-accent-text"
      outline
    />
    <div v-if="sent" class="mc-feedback">Resposta enviada!</div>
  </div>
</template>

<style scoped>
:root {
  --soft-primary: #e3f2fd;
  --soft-primary-text: #1976d2;
  --soft-grey: #f5f6fa;
  --soft-grey-text: #555;
  --soft-accent: #fff3e0;
  --soft-accent-text: #e65100;
  --soft-selected-bg: #ffe066;
  --soft-selected-text: #111;
}
.mc-preview-container {
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
  flex-direction: column;
  gap: 0;
}
.mc-alternatives-row {
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
.selected-btn {
  /* Cor principal via :style inline */
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
.mc-send-btn {
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 10px;
}
.soft-send-btn.q-btn--outline {
  background: var(--soft-accent) !important;
  color: var(--soft-accent-text) !important;
  border: 2px solid #ffe0b2;
}
.mc-feedback {
  color: #21ba45;
  margin-top: 18px;
  text-align: center;
  font-weight: 600;
}
@media (max-width: 800px) {
  .mc-alternatives-row {
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
