<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type {IQuizItem} from "../../services/quiz/types/IQuizItem.ts";
import QuizPreview from '../previews/QuizPreview.vue'
import SlidePreview from '../previews/SlidePreview.vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const fileInput = ref<HTMLInputElement | null>(null)
const fileInput1 = ref<HTMLInputElement | null>(null)
const fileInput2 = ref<HTMLInputElement | null>(null)

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

const validAlternatives = computed(() => {
  return formData.value.alternatives?.filter((alt: string) => alt.trim() !== '') || []
})

function getDefaultData(type: string) {
  const defaults: Record<string, any> = {
    QUIZ_MULTIPLE_CHOICE: {
      type: 'QUIZ_MULTIPLE_CHOICE',
      contentQuestion: '',
      alternatives: [''],
      answers: [],
      timerSeconds: 30,
      reward: 50
    },
    QUIZ_FILL_SPACE: {
      type: 'QUIZ_FILL_SPACE',
      contentQuestion: '',
      answers: [],
      timerSeconds: 30,
      reward: 50
    },
    QUIZ_OPEN: {
      type: 'QUIZ_OPEN',
      contentQuestion: '',
      quantityCharacters: 50,
      answers: [],
      timerSeconds: 30,
      reward: 50
    },
    QUIZ_POLL: {
      type: 'QUIZ_POLL',
      contentQuestion: '',
      answers: [],
      timerSeconds: 30,
      reward: 50
    },
    QUIZ_TRUE_FALSE: {
      type: 'QUIZ_TRUE_FALSE',
      contentQuestion: '',
      answers: ["true"],
      timerSeconds: 30,
      reward: 50
    },
    QUIZ_WORD_CLOUD: {
      type: 'QUIZ_WORD_CLOUD',
      contentQuestion: '',
      quantityCharacters: 50,
      answers: [],
      timerSeconds: 30,
      reward: 50
    },
    SLIDE_TITLE_1: {
      type: 'SLIDE_TITLE_1',
      contentTitle: ''
    },
    SLIDE_TITLE_2: {
      type: 'SLIDE_TITLE_2',
      contentTitle: '',
      contentDescription: ''
    },
    SLIDE_TEXT_1: {
      type: 'SLIDE_TEXT_1',
      contentHeader: '',
      contentSubHeaderOne: '',
      contentTextOne: ''
    },
    SLIDE_TEXT_2: {
      type: 'SLIDE_TEXT_2',
      contentHeader: '',
      contentSubHeaderOne: '',
      contentTextOne: '',
      contentSubHeaderTwo: '',
      contentTextTwo: ''
    },
    SLIDE_TEXT_MEDIA_1: {
      type: 'SLIDE_TEXT_MEDIA_1',
      contentHeader: '',
      contentTextOne: '',
      contentMediaOne: ''
    },
    SLIDE_TEXT_MEDIA_2: {
      type: 'SLIDE_TEXT_MEDIA_2',
      contentSubHeaderOne: '',
      contentTextOne: '',
      contentMediaOne: '',
      contentSubHeaderTwo: '',
      contentTextTwo: '',
      contentMediaTwo: ''
    }
    // ... Outros tipos permanecem iguais
  }

  return { ...defaults[type] }
}

function formatTypeName(type: string) {
  return type.replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase())
}

function saveItem() {
  // Validação para QUIZ types - usar props.type em vez de type
  if (props.type.startsWith('QUIZ_')) {
    // Validação comum para todos os tipos de QUIZ
    if (!formData.value.contentQuestion?.trim()) {
      $q.notify({
        type: 'warning',
        message: 'Question is required'
      })
      return
    }

    // Validações específicas para cada tipo de QUIZ
    if (props.type === 'QUIZ_MULTIPLE_CHOICE') {
      if (validAlternatives.value.length < 2) {
        $q.notify({
          type: 'warning',
          message: 'At least 2 alternatives are required'
        })
        return
      }

      if (formData.value.answers.length === 0) {
        $q.notify({
          type: 'warning',
          message: 'At least one correct answer must be selected'
        })
        return
      }
    }
    else if (props.type === 'QUIZ_FILL_SPACE' || props.type === 'QUIZ_POLL' || props.type === 'QUIZ_OPEN' || props.type === 'QUIZ_WORD_CLOUD') {
      if (formData.value.answers.length < 1) {
        $q.notify({
          type: 'warning',
          message: 'At least one answer/option is required'
        })
        return
      }

      // Verifica se todas as respostas estão preenchidas
      const emptyAnswers = formData.value.answers.some((answer: string) => !answer.trim())
      if (emptyAnswers) {
        $q.notify({
          type: 'warning',
          message: 'All answers/options must be filled'
        })
        return
      }
    }
    else if (props.type === 'QUIZ_TRUE_FALSE') {
      if (!formData.value.answers[0]) {
        $q.notify({
          type: 'warning',
          message: 'Please select the correct answer'
        })
        return
      }
    }

    // Validação para timer e reward
    if (formData.value.timerSeconds < 5 || formData.value.timerSeconds > 120) {
      $q.notify({
        type: 'warning',
        message: 'Timer must be between 5 and 120 seconds'
      })
      return
    }

    if (formData.value.reward < 10 || formData.value.reward > 100) {
      $q.notify({
        type: 'warning',
        message: 'Reward must be between 10 and 100 points'
      })
      return
    }
  }

  // Validação para SLIDE types - usar props.type em vez de type
  else if (props.type.startsWith('SLIDE_')) {
    if (props.type === 'SLIDE_TITLE_1' || props.type === 'SLIDE_TITLE_2') {
      if (!formData.value.contentTitle?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Title is required'
        })
        return
      }

      if (props.type === 'SLIDE_TITLE_2' && !formData.value.contentDescription?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Description is required'
        })
        return
      }
    }
    else if (props.type === 'SLIDE_TEXT_1') {
      if (!formData.value.contentHeader?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Header is required'
        })
        return
      }

      if (!formData.value.contentSubHeaderOne?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Sub header is required'
        })
        return
      }

      if (!formData.value.contentTextOne?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Main text is required'
        })
        return
      }
    }
    else if (props.type === 'SLIDE_TEXT_2') {
      if (!formData.value.contentHeader?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Header is required'
        })
        return
      }

      if (!formData.value.contentSubHeaderOne?.trim() || !formData.value.contentSubHeaderTwo?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Sub header is required'
        })
        return
      }

      if (!formData.value.contentTextOne?.trim() || !formData.value.contentTextTwo?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Both content sections must be filled'
        })
        return
      }
    }
    else if (props.type === 'SLIDE_TEXT_MEDIA_1') {
      if (!formData.value.contentHeader?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Header is required'
        })
        return
      }

      if (!formData.value.contentTextOne?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Main text is required'
        })
        return
      }

      if (!formData.value.contentMediaOne) {
        $q.notify({
          type: 'warning',
          message: 'Media content is required'
        })
        return
      }
    }
    else if (props.type === 'SLIDE_TEXT_MEDIA_2') {
      if (!formData.value.contentTextOne?.trim() || !formData.value.contentTextTwo?.trim()) {
        $q.notify({
          type: 'warning',
          message: 'Both content texts are required'
        })
        return
      }

      if (!formData.value.contentMediaOne || !formData.value.contentMediaTwo) {
        $q.notify({
          type: 'warning',
          message: 'Both media contents are required'
        })
        return
      }
    }
  }

  // Para QUIZ_MULTIPLE_CHOICE
  if (formData.value.type === 'QUIZ_MULTIPLE_CHOICE') {
    formData.value.alternatives = validAlternatives.value
    formData.value.answers = formData.value.answers.filter((answer: string) =>
        formData.value.alternatives.includes(answer)
    )
  }
  // Para QUIZ_FILL_SPACE e QUIZ_POLL
  else if (formData.value.type === 'QUIZ_FILL_SPACE' || formData.value.type === 'QUIZ_POLL') {
    formData.value.answers = formData.value.answers.filter((answer: string) =>
        answer.trim() !== ''
    )
  }
  // Para QUIZ_OPEN e QUIZ_WORD_CLOUD
  else if (formData.value.type === 'QUIZ_OPEN' || formData.value.type === 'QUIZ_WORD_CLOUD') {
    formData.value.answers = formData.value.answers.filter((answer: string) =>
        answer.trim() !== ''
    )
    formData.value.quantityCharacters = Math.max(0, formData.value.quantityCharacters)
  }
  // Para QUIZ_TRUE_FALSE - não precisa de filtro

  emit('save', formData.value)
  emit('close')
}

function addAlternative() {
  formData.value.alternatives.push('')
}

function removeAlternative(index: number) {
  const removedAlt = formData.value.alternatives[index]
  formData.value.alternatives.splice(index, 1)
  formData.value.answers = formData.value.answers.filter(
      (answer: string) => answer !== removedAlt
  )
}

function triggerFileInput(refName: string) {
  if (refName === 'fileInput1') {
    fileInput1.value?.click()
  } else if (refName === 'fileInput2') {
    fileInput2.value?.click()
  } else {
    fileInput.value?.click()
  }
}

function handleImageUpload(event: Event, field: string) {
  const input = event.target as HTMLInputElement
  if (input.files && input.files[0]) {
    const file = input.files[0]

    // Verifica se é uma imagem
    if (!file.type.match('image.*')) {
      $q.notify({
        type: 'negative',
        message: 'Por favor, selecione um arquivo de imagem válido (JPEG, PNG, GIF)'
      })
      return
    }

    // Verifica o tamanho do arquivo (limite de 2MB)
    if (file.size > 2 * 1024 * 1024) {
      $q.notify({
        type: 'negative',
        message: 'A imagem deve ter menos de 2MB'
      })
      return
    }

    // Converte para base64
    const reader = new FileReader()
    reader.onload = (e) => {
      formData.value[field] = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

function removeImage(field: string) {
  formData.value[field] = ''
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
        <div class="text-h6">{{ formatTypeName(type) }}</div>
        <q-space />
        <q-btn dense flat icon="close" @click="emit('close')">
          <q-tooltip class="bg-white text-primary">Close</q-tooltip>
        </q-btn>
      </q-bar>

      <q-card-section>
        <!-- Quiz Types -->
        <div v-if="type.startsWith('QUIZ_')">
          <!-- Question Input -->
          <q-input
              v-model="formData.contentQuestion"
              label="Question"
              color="yellow-14"
              class="q-mb-md"
              type="text"
          />

          <!-- Multiple Choice Specific Fields -->
          <div v-if="type === 'QUIZ_MULTIPLE_CHOICE'">
            <!-- Alternatives Section -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm flex items-center justify-between">
                <span>Alternatives</span>
                <q-btn
                    label="Add Alternative"
                    color="yellow-14"
                    text-color="black"
                    size="sm"
                    @click="addAlternative"
                />
              </div>

              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div v-for="(_, index) in formData.alternatives" :key="index" class="q-mb-sm row items-center">
                  <div class="col-grow row items-center">
                    <q-input
                        v-model="formData.alternatives[index]"
                        :label="`Alternative ${index + 1}`"
                        color="yellow-14"
                        class="col"
                        dense
                        :rules="[val => !!val.trim() || 'Required field']"
                    />
                    <q-btn
                        round
                        dense
                        flat
                        icon="remove"
                        color="negative"
                        @click="removeAlternative(index)"
                        v-if="formData.alternatives.length > 1"
                        class="q-ml-sm"
                    />
                  </div>
                </div>
              </q-card>
            </div>

            <!-- Answers Section -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm">Correct Answers</div>
              <q-card bordered flat class="bg-grey-8 q-pa-sm">
                <q-select
                    v-model="formData.answers"
                    :options="validAlternatives"
                    multiple
                    use-chips
                    color="yellow-14"
                    label="Select correct answers"
                    :rules="[
                    val => val.length > 0 || 'Select at least one correct answer',
                    val => val.every((ans:string) => validAlternatives.includes(ans)) || 'Invalid answer selected'
                  ]"
                    map-options
                    emit-value
                />
              </q-card>
            </div>
          </div>

          <!-- Fill Space Implementation -->
          <div v-else-if="type === 'QUIZ_FILL_SPACE'">
            <!-- Answers Section -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm flex items-center justify-between">
                <span>Correct Answers</span>
                <q-btn
                    label="Add Answer"
                    color="yellow-14"
                    text-color="black"
                    size="sm"
                    @click="formData.answers.push('')"
                />
              </div>

              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div v-for="(_, index) in formData.answers" :key="index" class="q-mb-sm row items-center">
                  <div class="col-grow row items-center">
                    <q-input
                        v-model="formData.answers[index]"
                        :label="`Answer ${index + 1}`"
                        color="yellow-14"
                        class="col"
                        dense
                        :rules="[val => !!val.trim() || 'Required field']"
                    />
                    <q-btn
                        round
                        dense
                        flat
                        icon="remove"
                        color="negative"
                        @click="formData.answers.splice(index, 1)"
                        v-if="formData.answers.length > 1"
                        class="q-ml-sm"
                    />
                  </div>
                </div>
              </q-card>
            </div>
          </div>

          <!-- Open Quiz Implementation -->
          <div v-else-if="type === 'QUIZ_OPEN'">
            <!-- Character Limit -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm">Answer Settings</div>
              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <!-- Character Limit Slider -->
                <div class="q-mb-md">
                  <div class="row items-center q-mb-sm">
                    <span class="col">Minimum characters required: {{ formData.quantityCharacters }}</span>
                    <q-input
                        v-model.number="formData.quantityCharacters"
                        type="number"
                        color="yellow-14"
                        dense
                        style="width: 80px"
                        class="q-ml-sm"
                        min="0"
                        max="500"
                    />
                  </div>
                  <q-slider
                      v-model="formData.quantityCharacters"
                      :min="0"
                      :max="500"
                      :step="10"
                      color="yellow-14"
                      label
                      label-always
                      :label-value="`${formData.quantityCharacters} chars`"
                  />
                  <div class="text-caption text-grey-4 q-mt-xs">
                    Set to 0 for no minimum character requirement
                  </div>
                </div>
              </q-card>
            </div>

            <!-- Sample Answers -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm flex items-center justify-between">
                <span>Sample Correct Answers</span>
                <q-btn
                    label="Add Sample"
                    color="yellow-14"
                    text-color="black"
                    size="sm"
                    @click="formData.answers.push('')"
                />
              </div>

              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div v-for="(_, index) in formData.answers" :key="index" class="q-mb-sm row items-center">
                  <div class="col-grow row items-center">
                    <q-input
                        v-model="formData.answers[index]"
                        :label="`Sample Answer ${index + 1}`"
                        color="yellow-14"
                        class="col"
                        dense
                    />
                    <q-btn
                        round
                        dense
                        flat
                        icon="remove"
                        color="negative"
                        @click="formData.answers.splice(index, 1)"
                        v-if="formData.answers.length > 0"
                        class="q-ml-sm"
                    />
                  </div>
                </div>
              </q-card>
            </div>
          </div>

          <!-- Poll Implementation -->
          <div v-else-if="type === 'QUIZ_POLL'">
            <!-- Poll Options -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm flex items-center justify-between">
                <span>Poll Options</span>
                <q-btn
                    label="Add Option"
                    color="yellow-14"
                    text-color="black"
                    size="sm"
                    @click="formData.answers.push('')"
                />
              </div>

              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div v-for="(_, index) in formData.answers" :key="index" class="q-mb-sm row items-center">
                  <div class="col-grow row items-center">
                    <q-input
                        v-model="formData.answers[index]"
                        :label="`Option ${index + 1}`"
                        color="yellow-14"
                        class="col"
                        dense
                        :rules="[val => !!val.trim() || 'Required field']"
                    />
                    <q-btn
                        round
                        dense
                        flat
                        icon="remove"
                        color="negative"
                        @click="formData.answers.splice(index, 1)"
                        v-if="formData.answers.length > 1"
                        class="q-ml-sm"
                    />
                  </div>
                </div>
              </q-card>

              <div class="text-caption text-grey-4 q-mt-xs">
                Note: Polls don't have correct answers, these are just the voting options
              </div>
            </div>
          </div>

          <!-- True/False Implementation -->
          <div v-else-if="type === 'QUIZ_TRUE_FALSE'">
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm">Correct Answer</div>
              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div class="row items-center q-gutter-md">
                  <q-radio
                      v-model="formData.answers[0]"
                      val="true"
                      label="True"
                      color="yellow-14"
                  />
                  <q-radio
                      v-model="formData.answers[0]"
                      val="false"
                      label="False"
                      color="yellow-14"
                  />
                </div>
              </q-card>
            </div>
          </div>

          <!-- Word Cloud Implementation -->
          <div v-else-if="type === 'QUIZ_WORD_CLOUD'">
            <!-- Character Limit -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm">Word Settings</div>
              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <!-- Character Limit Slider -->
                <div class="q-mb-md">
                  <div class="row items-center q-mb-sm">
                    <span class="col">Maximum characters per word: {{ formData.quantityCharacters }}</span>
                    <q-input
                        v-model.number="formData.quantityCharacters"
                        type="number"
                        color="yellow-14"
                        dense
                        style="width: 80px"
                        class="q-ml-sm"
                        min="1"
                        max="100"
                    />
                  </div>
                  <q-slider
                      v-model="formData.quantityCharacters"
                      :min="1"
                      :max="100"
                      :step="1"
                      color="yellow-14"
                      label
                      label-always
                      :label-value="`${formData.quantityCharacters} chars`"
                  />
                  <div class="text-caption text-grey-4 q-mt-xs">
                    Maximum length for each word in the cloud
                  </div>
                </div>
              </q-card>
            </div>

            <!-- Sample Words -->
            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm flex items-center justify-between">
                <span>Sample example correct Words</span>
                <q-btn
                    label="Add Word"
                    color="yellow-14"
                    text-color="black"
                    size="sm"
                    @click="formData.answers.push('')"
                />
              </div>

              <q-card bordered flat class="bg-grey-8 q-pa-sm q-mb-md">
                <div v-for="(_, index) in formData.answers" :key="index" class="q-mb-sm row items-center">
                  <div class="col-grow row items-center">
                    <q-input
                        v-model="formData.answers[index]"
                        :label="`Word ${index + 1}`"
                        color="yellow-14"
                        class="col"
                        dense
                        :rules="[
                      val => !!val.trim() || 'Word cannot be empty',
                      val => val.length <= formData.quantityCharacters ||
                            `Word too long (max ${formData.quantityCharacters} chars)`
                    ]"
                    />
                    <q-btn
                        round
                        dense
                        flat
                        icon="remove"
                        color="negative"
                        @click="formData.answers.splice(index, 1)"
                        v-if="formData.answers.length > 0"
                        class="q-ml-sm"
                    />
                  </div>
                </div>
              </q-card>

              <div class="text-caption text-grey-4 q-mt-xs">
                These words will appear larger in the word cloud visualization
              </div>
            </div>
          </div>

          <!-- Timer and Reward - Versão com Sliders -->
          <div class="q-mt-md">
            <div class="text-subtitle1 q-mb-sm">Timer and Reward</div>
            <q-card bordered flat class="bg-grey-8 q-pa-sm">
              <!-- Timer Slider -->
              <div class="q-mb-md">
                <div class="row items-center q-mb-sm">
                  <span class="col">Timer: {{ formData.timerSeconds }} seconds</span>
                  <q-input
                      v-model.number="formData.timerSeconds"
                      type="number"
                      color="yellow-14"
                      dense
                      style="width: 80px"
                      class="q-ml-sm"
                      min="5"
                      max="120"
                  />
                </div>
                <q-slider
                    v-model="formData.timerSeconds"
                    :min="5"
                    :max="120"
                    :step="5"
                    color="yellow-14"
                    label
                    label-always
                    :label-value="`${formData.timerSeconds}s`"
                />
              </div>

              <!-- Reward Slider -->
              <div>
                <div class="row items-center q-mb-sm">
                  <span class="col">Reward: {{ formData.reward }} points</span>
                  <q-input
                      v-model.number="formData.reward"
                      type="number"
                      color="yellow-14"
                      dense
                      style="width: 80px"
                      class="q-ml-sm"
                      min="10"
                      max="100"
                      :step="10"
                  />
                </div>
                <q-slider
                    v-model="formData.reward"
                    :min="10"
                    :max="100"
                    :step="10"
                    color="yellow-14"
                    label
                    label-always
                    :label-value="`${formData.reward}pts`"
                />
              </div>
            </q-card>
          </div>

        </div>

        <!-- Slide Types -->
        <div v-else-if="type.startsWith('SLIDE_')">
          <!-- Title Slide Implementation -->
          <div v-if="type === 'SLIDE_TITLE_1'">
            <q-input
                v-model="formData.contentTitle"
                label="Slide Title"
                color="yellow-14"
                class="q-mb-md"
                type="text"
                :rules="[
                  val => !!val?.trim() || 'Field is required',
                  val => val?.length <= 100 || 'Maximum 100 characters'
                ]"
            />

            <div class="text-caption text-grey-4 q-mt-sm">
              This is a title slide that starts your presentation
            </div>
          </div>

          <!-- Title Slide 2 Implementation -->
          <div v-else-if="type === 'SLIDE_TITLE_2'">
            <q-input
                v-model="formData.contentTitle"
                label="Slide Title"
                color="yellow-14"
                class="q-mb-md"
                type="text"
                :rules="[
                  val => !!val?.trim() || 'Field is required',
                  val => val?.length <= 100 || 'Maximum 100 characters'
                ]"
            />

            <q-input
                v-model="formData.contentDescription"
                label="Description"
                color="yellow-14"
                type="textarea"
                autogrow
                class="q-mb-md"
                :rules="[val => !!val.trim() || 'Description is required']"
            />

            <div class="text-caption text-grey-4 q-mt-sm">
              This is a title slide with subtitle for your presentation
            </div>
          </div>

          <!-- Text Slide Implementation -->
          <div v-else-if="type === 'SLIDE_TEXT_1'">
            <q-input
                v-model="formData.contentHeader"
                label="Header"
                color="yellow-14"
                class="q-mb-md"
                type="text"
                :rules="[val => !!val.trim() || 'Header is required']"
            />

            <q-input
                v-model="formData.contentSubHeaderOne"
                label="Subheader"
                color="yellow-14"
                class="q-mb-md"
                type="text"
            />

            <q-input
                v-model="formData.contentTextOne"
                label="Main Text"
                color="yellow-14"
                type="textarea"
                autogrow
                class="q-mb-md"
                :rules="[
                  val => !!val?.trim() || 'Field is required',
                  val => val?.length <= 500 || 'Maximum 500 characters'
                ]"
            />

            <div class="text-caption text-grey-4 q-mt-sm">
              This is a text slide for detailed content in your presentation
            </div>
          </div>

          <!-- Text Slide 2 Implementation -->
          <div v-else-if="type === 'SLIDE_TEXT_2'">
            <q-input
                v-model="formData.contentHeader"
                label="Header"
                color="yellow-14"
                class="q-mb-md"
                type="text"
                :rules="[val => !!val.trim() || 'Header is required']"
            />

            <div class="q-mb-md">
              <div class="text-subtitle2 q-mb-sm">First Content Section</div>
              <q-input
                  v-model="formData.contentSubHeaderOne"
                  label="Subheader"
                  color="yellow-14"
                  class="q-mb-sm"
                  type="text"
              />
              <q-input
                  v-model="formData.contentTextOne"
                  label="Content Text"
                  color="yellow-14"
                  type="textarea"
                  autogrow
                  :rules="[
                    val => !!val?.trim() || 'Field is required',
                    val => val?.length <= 500 || 'Maximum 500 characters'
                  ]"
              />
            </div>

            <div class="q-mb-md">
              <div class="text-subtitle2 q-mb-sm">Second Content Section</div>
              <q-input
                  v-model="formData.contentSubHeaderTwo"
                  label="Subheader"
                  color="yellow-14"
                  class="q-mb-sm"
                  type="text"
              />
              <q-input
                  v-model="formData.contentTextTwo"
                  label="Content Text"
                  color="yellow-14"
                  type="textarea"
                  autogrow
                  :rules="[val => !!val.trim() || 'Content text is required']"
              />
            </div>

            <div class="text-caption text-grey-4 q-mt-sm">
              This slide contains two content sections with their own subheaders and text
            </div>
          </div>

          <!-- Slide Text Media 1 Implementation -->
          <div v-else-if="type === 'SLIDE_TEXT_MEDIA_1'">
            <q-input
                v-model="formData.contentHeader"
                label="Header"
                color="yellow-14"
                class="q-mb-md"
                type="text"
                :rules="[
                  val => !!val?.trim() || 'Field is required',
                  val => val?.length <= 100 || 'Maximum 100 characters'
                ]"
            />

            <q-input
                v-model="formData.contentTextOne"
                label="Main Text"
                color="yellow-14"
                type="textarea"
                autogrow
                class="q-mb-md"
                :rules="[
                  val => !!val?.trim() || 'Field is required',
                  val => val?.length <= 500 || 'Maximum 500 characters'
                ]"
            />

            <div class="q-mt-md">
              <div class="text-subtitle1 q-mb-sm">Media Content</div>
              <q-card bordered flat class="bg-grey-8 q-pa-sm">
                <!-- Input de arquivo invisível -->
                <input
                    type="file"
                    ref="fileInput"
                    accept="image/*"
                    @change="(e) => handleImageUpload(e, 'contentMediaOne')"
                    style="display: none"
                />

                <div v-if="!formData.contentMediaOne">
                  <q-btn
                      label="Upload Image"
                      color="yellow-14"
                      text-color="black"
                      icon="upload"
                      @click="triggerFileInput('fileInput')"
                      class="q-mb-sm"
                  />
                  <div class="text-caption text-grey-4">
                    Formatos suportados: JPG, PNG, GIF. Tamanho máximo: 2MB
                  </div>
                </div>

                <div v-else>
                  <div class="row items-center q-mb-sm">
                    <q-img
                        :src="formData.contentMediaOne"
                        style="max-height: 100px; max-width: 150px"
                        class="q-mr-sm"
                    />
                    <q-btn
                        label="Change Image"
                        color="yellow-14"
                        text-color="black"
                        size="sm"
                        @click="triggerFileInput('fileInput')"
                        class="q-mr-sm"
                    />
                    <q-btn
                        label="Remove"
                        color="negative"
                        size="sm"
                        @click="removeImage('contentMediaOne')"
                    />
                  </div>
                </div>
              </q-card>
            </div>

            <div class="text-caption text-grey-4 q-mt-sm">
              This is a slide with text and media content
            </div>
          </div>

          <!-- Slide Text Media 2 Implementation -->
          <div v-else-if="type === 'SLIDE_TEXT_MEDIA_2'">
            <!-- First Section -->
            <div class="q-mb-md">
              <div class="text-subtitle2 q-mb-sm">First Content Section</div>
              <q-input
                  v-model="formData.contentSubHeaderOne"
                  label="Subheader"
                  color="yellow-14"
                  class="q-mb-sm"
                  type="text"
                  :rules="[
                    val => !!val?.trim() || 'Field is required',
                    val => val?.length <= 100 || 'Maximum 100 characters'
                  ]"
              />
              <q-input
                  v-model="formData.contentTextOne"
                  label="Content Text"
                  color="yellow-14"
                  type="textarea"
                  autogrow
                  class="q-mb-sm"
                  :rules="[
                    val => !!val?.trim() || 'Field is required',
                    val => val?.length <= 500 || 'Maximum 500 characters'
                  ]"
              />

              <div class="q-mt-sm">
                <div class="text-subtitle3 q-mb-xs">Media Content</div>
                <q-card bordered flat class="bg-grey-8 q-pa-sm">
                  <!-- Input de arquivo invisível para a primeira imagem -->
                  <input
                      type="file"
                      ref="fileInput1"
                      accept="image/*"
                      @change="(e) => handleImageUpload(e, 'contentMediaOne')"
                      style="display: none"
                  />

                  <div v-if="!formData.contentMediaOne">
                    <q-btn
                        label="Upload Image"
                        color="yellow-14"
                        text-color="black"
                        icon="upload"
                        @click="() => triggerFileInput('fileInput1')"
                        class="q-mb-sm"
                    />
                    <div class="text-caption text-grey-4">
                      Formatos suportados: JPG, PNG, GIF. Tamanho máximo: 2MB
                    </div>
                  </div>

                  <div v-else>
                    <div class="row items-center q-mb-sm">
                      <q-img
                          :src="formData.contentMediaOne"
                          style="max-height: 100px; max-width: 150px"
                          class="q-mr-sm"
                      />
                      <q-btn
                          label="Change Image"
                          color="yellow-14"
                          text-color="black"
                          size="sm"
                          @click="() => triggerFileInput('fileInput1')"
                          class="q-mr-sm"
                      />
                      <q-btn
                          label="Remove"
                          color="negative"
                          size="sm"
                          @click="() => removeImage('contentMediaOne')"
                      />
                    </div>
                  </div>
                </q-card>
              </div>
            </div>

            <!-- Second Section -->
            <div class="q-mb-md">
              <div class="text-subtitle2 q-mb-sm">Second Content Section</div>
              <q-input
                  v-model="formData.contentSubHeaderTwo"
                  label="Subheader"
                  color="yellow-14"
                  class="q-mb-sm"
                  type="text"
                  :rules="[
                    val => !!val?.trim() || 'Field is required',
                    val => val?.length <= 100 || 'Maximum 100 characters'
                  ]"
              />
              <q-input
                  v-model="formData.contentTextTwo"
                  label="Content Text"
                  color="yellow-14"
                  type="textarea"
                  autogrow
                  class="q-mb-sm"
                  :rules="[
                    val => !!val?.trim() || 'Field is required',
                    val => val?.length <= 500 || 'Maximum 500 characters'
                  ]"
              />

              <div class="q-mt-sm">
                <div class="text-subtitle3 q-mb-xs">Media Content</div>
                <q-card bordered flat class="bg-grey-8 q-pa-sm">
                  <!-- Input de arquivo invisível para a segunda imagem -->
                  <input
                      type="file"
                      ref="fileInput2"
                      accept="image/*"
                      @change="(e) => handleImageUpload(e, 'contentMediaTwo')"
                      style="display: none"
                  />

                  <div v-if="!formData.contentMediaTwo">
                    <q-btn
                        label="Upload Image"
                        color="yellow-14"
                        text-color="black"
                        icon="upload"
                        @click="() => triggerFileInput('fileInput2')"
                        class="q-mb-sm"
                    />
                    <div class="text-caption text-grey-4">
                      Formatos suportados: JPG, PNG, GIF. Tamanho máximo: 2MB
                    </div>
                  </div>

                  <div v-else>
                    <div class="row items-center q-mb-sm">
                      <q-img
                          :src="formData.contentMediaTwo"
                          style="max-height: 100px; max-width: 150px"
                          class="q-mr-sm"
                      />
                      <q-btn
                          label="Change Image"
                          color="yellow-14"
                          text-color="black"
                          size="sm"
                          @click="() => triggerFileInput('fileInput2')"
                          class="q-mr-sm"
                      />
                      <q-btn
                          label="Remove"
                          color="negative"
                          size="sm"
                          @click="() => removeImage('contentMediaTwo')"
                      />
                    </div>
                  </div>
                </q-card>
              </div>
            </div>

            <div class="text-caption text-grey-4 q-mt-sm">
              This is a slide with two content sections, each with text and media
            </div>
          </div>

        </div>
      </q-card-section>

      <!-- Preview -->
      <q-card-section class="bg-grey-8 q-pa-md">
        <div class="text-subtitle2 q-mb-sm">Live Preview</div>
        <QuizPreview
            v-if="type.startsWith('QUIZ_')"
            :type="type"
            :data="formData"
        />
        <SlidePreview
            v-else-if="type.startsWith('SLIDE_')"
            :type="type"
            :data="formData"
        />
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