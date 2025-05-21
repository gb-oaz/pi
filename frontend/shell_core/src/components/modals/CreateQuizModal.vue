<script setup lang="ts">
import { ref } from 'vue'
import { useQuasar } from 'quasar'
import { useCreateQuizStore } from '../../stores/useCreateQuizStore.ts'
import QuizItemEditorModal from "./QuizItemEditorModal.vue";

const $q = useQuasar()
const createQuizStore = useCreateQuizStore()

const dialog = ref(false)
const maximizedToggle = ref(true)
const currentEditingIndex = ref<number | null>(null)
const currentItemType = ref<string>('')
const dragItemIndex = ref<number | null>(null)
const dragOverItemIndex = ref<number | null>(null)

function open(){ dialog.value = true }
function close(){ dialog.value = false }

function openQuizTypeSelector() {
  $q.bottomSheet({
    dark: true,
    title: 'Select Quiz Type',
    grid: true,
    actions: [
      // Quiz Types
      { label: 'Multiple Choice', icon: 'list', id: 'QUIZ_MULTIPLE_CHOICE' },
      { label: 'Fill Space', icon: 'edit', id: 'QUIZ_FILL_SPACE' },
      { label: 'True/False', icon: 'check', id: 'QUIZ_TRUE_FALSE' },
      { label: 'Open Answer', icon: 'short_text', id: 'QUIZ_OPEN' },
      { label: 'Poll', icon: 'poll', id: 'QUIZ_POLL' },
      { label: 'Word Cloud', icon: 'cloud', id: 'QUIZ_WORD_CLOUD' },
      // Slide Types
      { label: 'Title Slide 1', icon: 'title', id: 'SLIDE_TITLE_1' },
      { label: 'Title Slide 2', icon: 'title', id: 'SLIDE_TITLE_2' },
      { label: 'Text Slide 1', icon: 'notes', id: 'SLIDE_TEXT_1' },
      { label: 'Text Slide 2', icon: 'notes', id: 'SLIDE_TEXT_2' },
      { label: 'Text + Media 1', icon: 'image', id: 'SLIDE_TEXT_MEDIA_1' },
      { label: 'Text + Media 2', icon: 'collections', id: 'SLIDE_TEXT_MEDIA_2' }
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
        <!-- Jumbotron Button -->
        <q-btn
            class="full-width q-mb-md"
            size="lg"
            color="yellow-14"
            text-color="black"
            icon="add"
            label="Add New Quiz Item"
            @click="openQuizTypeSelector"
            style="height: 80px; font-size: 1.2rem;"
        />

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
.type-quiz-multiple-choice
  background: linear-gradient(to bottom right, $indigo-6, $indigo-8)
.type-quiz-fill-space
  background: linear-gradient(to bottom right, $teal-6, $teal-8)
.type-quiz-true-false
  background: linear-gradient(to bottom right, $purple-6, $purple-8)
.type-quiz-open
  background: linear-gradient(to bottom right, $blue-6, $blue-8)
.type-quiz-poll
  background: linear-gradient(to bottom right, $orange-6, $orange-8)
.type-quiz-word-cloud
  background: linear-gradient(to bottom right, $cyan-6, $cyan-8)

// Slide types
.type-slide-title-1, .type-slide-title-2
  background: linear-gradient(to bottom right, $red-6, $red-8)
.type-slide-text-1, .type-slide-text-2
  background: linear-gradient(to bottom right, $green-6, $green-8)
.type-slide-text-media-1, .type-slide-text-media-2
  background: linear-gradient(to bottom right, $amber-6, $amber-8)
</style>