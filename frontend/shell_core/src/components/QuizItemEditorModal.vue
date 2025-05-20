<script setup lang="ts">
import { ref, watch, computed } from 'vue'
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
                <span>Sample Correct Answers (optional)</span>
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
                <span>Example Words (optional)</span>
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