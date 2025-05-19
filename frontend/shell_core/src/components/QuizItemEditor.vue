<script setup lang="ts">
import { ref, watch } from 'vue'
import type {IQuizItem} from "../services/quiz/types/IQuizItem.ts";

const props = defineProps({
  item: {
    type: Object as () => IQuizItem | null,
    default: null
  },
  type: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['save', 'close'])

const formData = ref(props.item || getDefaultData(props.type))
const show = ref(true)

function getDefaultData(type: string) {
  const defaults: Record<string, any> = {
    // Quiz Types
    QUIZ_MULTIPLE_CHOICE: {
      type: 'QUIZ_MULTIPLE_CHOICE',
      contentQuestion: '',
      alternatives: ['', '', '', ''],
      answers: [],
      timerSeconds: 30,
      reward: 10
    },
    QUIZ_FILL_SPACE: {
      type: 'QUIZ_FILL_SPACE',
      contentQuestion: '',
      answers: [''],
      timerSeconds: 25,
      reward: 15
    },
    // ... outros tipos de quiz
    // Slide Types
    SLIDE_TITLE_1: {
      type: 'SLIDE_TITLE_1',
      contentTitle: ''
    },
    SLIDE_TITLE_2: {
      type: 'SLIDE_TITLE_2',
      contentTitle: '',
      contentDescription: ''
    }
    // ... outros tipos de slide
  }

  return { ...defaults[type] }
}

const saveItem = () => {
  emit('save', formData.value)
  emit('close')
}

watch(() => show.value, (val) => {
  if (!val) {
    emit('close')
  }
})
</script>

<template>
  <q-dialog v-model="show" persistent>
    <q-card class="bg-grey-9 text-white" style="width: 700px; max-width: 80vw;">
      <q-bar>
        <div class="text-h6">Edit {{ type }}</div>
        <q-space />
        <q-btn dense flat icon="close" @click="emit('close')">
          <q-tooltip class="bg-white text-primary">Close</q-tooltip>
        </q-btn>
      </q-bar>

      <q-card-section>
        <!-- Formulário dinâmico baseado no tipo -->
        <div v-if="type.startsWith('QUIZ_')">
          <q-input
              v-model="formData.contentQuestion"
              label="Question"
              color="yellow-14"
              class="q-mb-md"
          />

          <!-- Campos específicos para cada tipo de quiz -->
          <div v-if="type === 'QUIZ_MULTIPLE_CHOICE'">
            <div v-for="(alt, index) in formData.alternatives" :key="index" class="q-mb-sm">
              <q-input
                  v-model="formData.alternatives[index]"
                  :label="`Alternative ${index + 1}`"
                  color="yellow-14"
              />
              <q-checkbox
                  v-model="formData.answers"
                  :val="alt"
                  label="Correct answer"
                  color="yellow-14"
              />
            </div>
          </div>

          <!-- Adicione outros tipos de quiz aqui -->
        </div>

        <!-- Formulário para slides -->
        <div v-else>
          <q-input
              v-if="formData.contentTitle"
              v-model="formData.contentTitle"
              label="Title"
              color="yellow-14"
              class="q-mb-md"
          />

          <!-- Adicione outros campos de slide aqui -->
        </div>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Cancel" color="white" @click="emit('close')" />
        <q-btn label="Save" color="yellow-14" text-color="black" @click="saveItem" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<style scoped lang="sass">

</style>