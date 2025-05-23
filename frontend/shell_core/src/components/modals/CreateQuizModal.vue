<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useQuasar } from 'quasar'
import { useCreateQuizStore } from '../../stores/useCreateQuizStore.ts'
import QuizItemEditorModal from "./QuizItemEditorModal.vue";
import { random } from '../../utils/images/Random.ts'
import { QuizApi } from '../../services/quiz/QuizApi.ts'

const randomBg = random.backgroundImages;
document.documentElement.style.setProperty('--random-background-cp', `url(${randomBg[1]})`);
document.documentElement.style.setProperty('--random-background-qp', `url(${randomBg[3]})`);

const $q = useQuasar()
const createQuizStore = useCreateQuizStore()
const quizApi = new QuizApi()

const dialog = ref(false)
const maximizedToggle = ref(true)
const currentEditingIndex = ref<number | null>(null)
const currentItemType = ref<string>('')
const dragItemIndex = ref<number | null>(null)
const dragOverItemIndex = ref<number | null>(null)
const availableCategories = ref<string[]>(['Math', 'Science', 'History', 'Geography', 'Language', 'Art', 'Technology'])
const selectedCategories = ref<string[]>([])

const quizName = computed({
  get: () => createQuizStore.quiz.name,
  set: (value) => createQuizStore.setQuizName(value)
})

function open(){ dialog.value = true }
function close(){ dialog.value = false }

function openQuizTypeSelector() {
  $q.bottomSheet({
    dark: true,
    title: 'Select Type',
    grid: true,
    actions: [
      // Slide Types
      { label: 'Title Slide 1', icon: 'title', id: 'SLIDE_TITLE_1' },
      { label: 'Title Slide 2', icon: 'title', id: 'SLIDE_TITLE_2' },
      { label: 'Text Slide 1', icon: 'notes', id: 'SLIDE_TEXT_1' },
      { label: 'Text Slide 2', icon: 'notes', id: 'SLIDE_TEXT_2' },
      { label: 'Text + Media 1', icon: 'image', id: 'SLIDE_TEXT_MEDIA_1' },
      { label: 'Text + Media 2', icon: 'collections', id: 'SLIDE_TEXT_MEDIA_2' },
      {},
      // Quiz Types
      { label: 'Multiple Choice', icon: 'list', id: 'QUIZ_MULTIPLE_CHOICE' },
      { label: 'Fill Space', icon: 'edit', id: 'QUIZ_FILL_SPACE' },
      { label: 'True/False', icon: 'check', id: 'QUIZ_TRUE_FALSE' },
      { label: 'Open Answer', icon: 'short_text', id: 'QUIZ_OPEN' },
      { label: 'Poll', icon: 'poll', id: 'QUIZ_POLL' },
      { label: 'Word Cloud', icon: 'cloud', id: 'QUIZ_WORD_CLOUD' }
    ]
  }).onOk((action) => {
    openQuizEditor(action.id)
  })
}

function openQuizEditor(type: string) {
  currentItemType.value = type
  if (currentEditingIndex.value !== null) {
    const existingItem = createQuizStore.quiz.quizes[currentEditingIndex.value]
    if (existingItem.type !== type) {
      createQuizStore.removeQuizItem(currentEditingIndex.value)
      currentEditingIndex.value = null
    }
  }
}

function handleSaveItem(itemData: any) {
  if (currentEditingIndex.value !== null) {
    createQuizStore.updateQuizItem(currentEditingIndex.value, itemData)
  } else {
    createQuizStore.addQuizItem(itemData)
  }
  currentEditingIndex.value = null
}

function editItem(index: number) {
  currentEditingIndex.value = index
  currentItemType.value = createQuizStore.quiz.quizes[index].type
}

function getItemPreview(item: any) {
  if (item.contentQuestion) return item.contentQuestion
  if (item.contentTitle) return item.contentTitle
  if (item.contentHeader) return item.contentHeader
  return 'Quiz item'
}

function formatTypeName(type: string) {
  return type.replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase())
}

function onDragStart(index: number) {
  dragItemIndex.value = index
}

function onDragOver(event: DragEvent, index: number) {
  event.preventDefault()
  dragOverItemIndex.value = index
}

function onDrop(index: number) {
  if (dragItemIndex.value === null || dragItemIndex.value === index) return
  const itemToMove = createQuizStore.quiz.quizes[dragItemIndex.value]
  createQuizStore.quiz.quizes.splice(dragItemIndex.value, 1)
  createQuizStore.quiz.quizes.splice(index, 0, itemToMove)

  dragItemIndex.value = null
  dragOverItemIndex.value = null
}

function onDragEnd() {
  dragItemIndex.value = null
  dragOverItemIndex.value = null
}

function getTypeClass(type: string) {
  return `type-${type.toLowerCase().replace(/_/g, '-')}`
}

function updateCategories() {
  createQuizStore.setQuizCategories([...selectedCategories.value])
}

async function clearQuiz() {
  $q.dialog({
    title: 'Confirm Clear Quiz',
    message: 'Are you sure you want to clear all quiz items? This action cannot be undone.',
    ok: {
      label: 'Yes, clear it',
      flat: true,
      color: 'negative',
      textColor: 'white'
    },
    cancel: {
      label: 'Cancel',
      flat: true,
      color: 'grey-8',
      textColor: 'white'
    },
    persistent: true,
    dark: true,
    class: 'bg-grey-10 text-white',
    style: {
      backgroundColor: '#1e1e1e',
      color: 'white',
      border: '1px solid #ffeb3b'
    },
    cardStyle: {
      minWidth: '350px',
      maxWidth: '500px',
      backgroundColor: '#1e1e1e',
      border: '1px solid #ffeb3b'
    }
  }).onOk(() => {
    createQuizStore.clearStorage()
    $q.notify({
      type: 'positive',
      message: 'Quiz cleared successfully',
      position: 'top',
      timeout: 2500
    })
  })
}

async function saveQuiz() {
  try {
    const nameValidation = validateQuizName(quizName.value)
    if (nameValidation !== true) {
      $q.notify({
        type: 'warning',
        message: nameValidation,
        position: 'top'
      })
      return
    }

    const categoriesValidation = validateCategories()
    if (categoriesValidation !== true) {
      $q.notify({
        type: 'warning',
        message: categoriesValidation,
        position: 'top'
      })
      return
    }

    updateCategories()
    const quiz = createQuizStore.quiz
    if (quiz.quizes.length === 0) {
      $q.notify({
        type: 'warning',
        message: 'Please add at least one quiz item',
        position: 'top'
      })
      return
    }

    $q.loading.show({
      message: 'Saving your quiz...'
    })

    let result: { key: string }

    if (quiz.key) {
      // Update existing quiz
      result = await quizApi.updateQuiz(quiz.key, quiz.name, quiz.categories)
    } else {
      // Create new quiz
      result = await quizApi.createQuiz(quiz.name, quiz.categories)
    }

    if (result.key) {
      // Save quiz items
      for (let i = 0; i < quiz.quizes.length; i++) {
        const item = quiz.quizes[i]
        if (item.id) {
          await quizApi.updateQuizItem(result.key, i + 1, item)
        } else {
          await quizApi.createQuizItem(result.key, i + 1, item)
        }
      }

      $q.notify({
        type: 'positive',
        message: 'Quiz saved successfully'
      })
      createQuizStore.clearStorage()
      close()
    }
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to save quiz'
    })
    console.error('Error saving quiz:', error)
  } finally {
    $q.loading.hide()
  }
}

function validateQuizName(val: string) {
  if (!val || val.trim() === '') {
    return 'Quiz name is required'
  }
  if (!/^[a-zA-ZÀ-ÿ\s'-]+$/.test(val)) {
    return 'Only letters (including accented characters), spaces, hyphens and apostrophes are allowed'
  }
  return true
}

function validateCategories() {
  if (selectedCategories.value.length === 0) {
    return 'At least one category must be selected'
  }
  return true
}

onMounted(() => {
  if (createQuizStore.quiz.name) {
    quizName.value = createQuizStore.quiz.name
  }
  if (createQuizStore.quiz.categories && createQuizStore.quiz.categories.length > 0) {
    selectedCategories.value = [...createQuizStore.quiz.categories]
  }
})

defineExpose({
  open,
  close
})
</script>

<template>
  <q-dialog
      v-model="dialog"
      persistent
      :maximized="maximizedToggle"
      transition-show="slide-up"
      transition-hide="slide-down"
  >
    <q-card class="bg-grey-10 text-white">
      <q-bar>
        <q-space />
        <q-btn dense flat icon="minimize" @click="maximizedToggle = false" :disable="!maximizedToggle">
          <q-tooltip v-if="maximizedToggle" class="bg-white text-primary">Minimize</q-tooltip>
        </q-btn>
        <q-btn dense flat icon="crop_square" @click="maximizedToggle = true" :disable="maximizedToggle">
          <q-tooltip v-if="!maximizedToggle" class="bg-white text-primary">Maximize</q-tooltip>
        </q-btn>
        <q-btn dense flat icon="close" @click="close">
          <q-tooltip class="bg-white text-primary">Close</q-tooltip>
        </q-btn>
      </q-bar>

      <q-card-section>
        <div class="text-h6">Create Quiz</div>
      </q-card-section>

      <q-card-section class="q-pt-none">

        <!-- nome e categorias -->
        <div class="row q-col-gutter-md q-mb-md">
          <div class="col-12 col-md-8">
            <q-input
                v-model="quizName"
                label="Quiz Name"
                dark
                filled
                color="yellow-14"
                :rules="[
                  val => !!val || 'Quiz name is required',
                  val => /^[a-zA-ZÀ-ÿ\s'-]+$/.test(val) || 'Only letters and spaces are allowed'
                ]"
                lazy-rules
            >
              <template v-slot:prepend>
                <q-icon name="title" />
              </template>
            </q-input>
          </div>
          <div class="col-12 col-md-4">
            <q-select
                v-model="selectedCategories"
                label="Categories"
                dark
                filled
                multiple
                use-chips
                color="yellow-14"
                :options="availableCategories"
                @update:model-value="updateCategories"
                :rules="[
                  val => val && val.length > 0 || 'At least one category is required'
                ]"
                lazy-rules
            >
              <template v-slot:prepend>
                <q-icon name="category" />
              </template>
            </q-select>
          </div>
        </div>


        <!-- Jumbotron Buttons Row -->
        <div class="row q-col-gutter-md q-mb-md">
          <div class="col">
            <q-btn
                class="full-width"
                size="lg"
                color="yellow-14"
                text-color="black"
                icon="add"
                label="Add Item"
                @click="openQuizTypeSelector"
                style="height: 80px; font-size: 1.2rem;"
            />
          </div>
          <div class="col">
            <q-btn
                class="full-width"
                size="lg"
                color="gray-10"
                text-color="white"
                icon="delete"
                label="Clear Quiz"
                @click="clearQuiz"
                style="height: 80px; font-size: 1.2rem;"
            />
          </div>
          <div class="col">
            <q-btn
                class="full-width"
                size="lg"
                color="green"
                text-color="white"
                icon="save"
                label="Save Quiz"
                @click="saveQuiz"
                style="height: 80px; font-size: 1.2rem;"
            />
          </div>
        </div>

        <!-- Lista de itens do quiz como grid com drag and drop -->
        <div class="row q-col-gutter-md">
          <div
              v-for="(item, index) in createQuizStore.quiz.quizes"
              :key="index"
              class="col-xs-12 col-sm-6 col-md-4 col-lg-3"
          >
            <q-card
                class="cursor-grab"
                :class="[
                    getTypeClass(item.type),
                    { 'drag-over': dragOverItemIndex === index, 'dragging': dragItemIndex === index }
                ]"
                draggable="true"
                @dragstart="onDragStart(index)"
                @dragover="onDragOver($event, index)"
                @drop="onDrop(index)"
                @dragend="onDragEnd"
            >
              <q-card-section>
                <div class="row items-center">
                  <div class="col">
                    <div class="text-subtitle1 text-truncate">{{ formatTypeName(item.type) }}</div>
                    <div class="text-caption text-grey-4 text-truncate">{{ getItemPreview(item) }}</div>
                  </div>
                  <div class="col-auto">
                    <q-btn flat round icon="edit" @click.stop="editItem(index)" />
                    <q-btn flat round icon="delete" @click.stop="createQuizStore.removeQuizItem(index)" />
                  </div>
                </div>
              </q-card-section>
              <q-tooltip>
                {{ getItemPreview(item) }}
              </q-tooltip>
            </q-card>
          </div>
        </div>
      </q-card-section>

      <!-- Modal de edição -->
      <QuizItemEditorModal
          v-if="currentItemType"
          :item="currentEditingIndex !== null ? createQuizStore.quiz.quizes[currentEditingIndex] : null"
          :type="currentItemType"
          @save="handleSaveItem"
          @close="currentEditingIndex = null; currentItemType = ''"
      />
    </q-card>
  </q-dialog>
</template>

<style scoped lang="sass">
.cursor-grab
  cursor: grab
  &:active
    cursor: grabbing

.q-card
  height: 100%
  transition: transform 0.2s, opacity 0.2s
  background: $grey-8  // Cor de fallback
  .text-subtitle1, .text-caption
    color: white
  .text-grey-4
    color: rgba(255, 255, 255, 0.7)
  &:hover
    transform: translateY(-2px)
    box-shadow: 0 4px 8px rgba(0,0,0,0.3)

.drag-over
  border: 2px dashed $yellow-6
  opacity: 0.7
  background: $grey-7

.dragging
  opacity: 0.5
  transform: scale(0.95)

// Quiz types
.type-quiz-multiple-choice,
.type-quiz-fill-space,
.type-quiz-true-false,
.type-quiz-open,
.type-quiz-poll,
.type-quiz-word-cloud
  background-image: var(--random-background-cp)
  background-size: cover
  background-position: center
  background-color: rgba(89, 84, 45, 0.56)
  text-shadow: #1D1D1D 0 0 5px
.type-slide-title-1,
.type-slide-title-2
  background-image: var(--random-background-qp)
  background-size: cover
  background-position: center
  background-color: rgba(61, 89, 46, 0.56)
  text-shadow: #1D1D1D 0 0 5px
.type-slide-text-1,
.type-slide-text-2
  background-image: var(--random-background-qp)
  background-size: cover
  background-position: center
  background-color: rgba(46, 53, 89, 0.56)
  text-shadow: #1D1D1D 0 0 5px
.type-slide-text-media-1,
.type-slide-text-media-2
  background-image: var(--random-background-qp)
  background-size: cover
  background-position: center
  background-color: rgba(89, 46, 51, 0.56)
  text-shadow: #1D1D1D 0 0 5px
</style>