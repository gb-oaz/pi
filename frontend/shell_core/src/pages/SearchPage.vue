<script setup lang="ts">
import { computed, onMounted, ref, watch, onBeforeUnmount, nextTick } from 'vue'
import { debounce } from 'quasar'
import { useAuthStore } from "../stores/authStore";
import type { IPageable } from '../utils/types/IPageable.ts'
import type { IQuiz } from '../services/quiz/types/IQuiz.ts'
import { QuizApi } from '../services/quiz/QuizApi.ts'
import { random } from '../utils/images/Random.ts'
import router from "../routes.ts";
import SearchLogin from '../components/login/SearchLogin.vue'
import QuizListings from '../components/lists/QuizListings.vue'

// Constants
const DEFAULT_CARDS_PER_PAGE = 4
const CARD_WIDTH_WITH_GAP = 362 + 15
const DEBOUNCE_DELAY = 500
const RESIZE_DEBOUNCE_DELAY = 200

// Reactive state
const searchTerm = ref('')
const currentPage = ref(1)
const quizListingsContainer = ref<HTMLElement | null>(null)
const cardsPerPage = ref(DEFAULT_CARDS_PER_PAGE)
const quizPageable = ref<IPageable<IQuiz>>(createEmptyPageable())
const quizMedia = ref(createEmptyQuizMedia())

const authStore = useAuthStore();
const quizApi = new QuizApi()

// Computed properties
const totalPages = computed(() =>
    Math.ceil(quizPageable.value.pagination.total / cardsPerPage.value)
)
const paginatedQuizzes = computed(() => quizPageable.value.content)

// Helper functions
function createEmptyPageable(): IPageable<IQuiz> {
  return {
    content: [],
    pagination: {
      page: 0,
      size: cardsPerPage.value,
      total: 0
    }
  }
}

function createEmptyQuizMedia() {
  return {
    images: {} as Record<string, string>,
    colors: {} as Record<string, string>
  }
}

function calculateCardsPerPage() {
  if (!quizListingsContainer.value) return

  const containerWidth = quizListingsContainer.value.clientWidth
  const cardsThatFit = Math.floor(containerWidth / CARD_WIDTH_WITH_GAP)
  cardsPerPage.value = Math.max(1, cardsThatFit)
}

async function fetchQuizzes(page: number = currentPage.value, search: string = searchTerm.value) {
  try {
    const response = await quizApi.getQuizes(
        undefined,
        undefined,
        search || undefined,
        undefined,
        page,
        cardsPerPage.value
    )

    quizPageable.value = response
    updateQuizMedia(response.content)
  } catch (error) {
    console.error('Failed to load quizzes:', error)
    quizPageable.value = createEmptyPageable()
  }
}

function updateQuizMedia(quizzes: IQuiz[]) {
  quizzes.forEach(quiz => {
    if (!quizMedia.value.images[quiz.key]) {
      quizMedia.value.images[quiz.key] = random.getRandomImage()
      quizMedia.value.colors[quiz.key] = random.getRandomColor()
    }
  })
}

function handleSearch() {
  currentPage.value = 1
  fetchQuizzes()
}

function handlePageChange(page: number) {
  currentPage.value = page
  fetchQuizzes()
}

function handlePlay(quiz: IQuiz) {
  console.log('Playing quiz:', quiz.name)
}
// Resize observer
let resizeObserver: ResizeObserver | null = null

function initResizeObserver() {
  if (!quizListingsContainer.value) return

  cleanupResizeObserver()

  resizeObserver = new ResizeObserver(debounce(() => {
    const oldValue = cardsPerPage.value
    calculateCardsPerPage()

    if (oldValue !== cardsPerPage.value) {
      fetchQuizzes()
    }
  }, RESIZE_DEBOUNCE_DELAY))

  resizeObserver.observe(quizListingsContainer.value)
}

function cleanupResizeObserver() {
  if (resizeObserver) {
    resizeObserver.disconnect()
    resizeObserver = null
  }
}

// Watchers
watch(searchTerm, debounce(() => {
  currentPage.value = 1
  fetchQuizzes()
}, DEBOUNCE_DELAY))

// Lifecycle hooks
onMounted(() => {
  nextTick(() => {
    calculateCardsPerPage()
    fetchQuizzes()
    initResizeObserver()
  })
})

onMounted(() => {
  if (!authStore.hasAnyRole(['ANONYMOUS', 'STUDENT', 'TEACHER'])) {
    router.push({ name: 'home' })
  }
})

onBeforeUnmount(() => {
  cleanupResizeObserver()
})
</script>

<template>
  <main class="article-container">
    <SearchLogin v-model="searchTerm" @search="handleSearch" />

    <q-separator color="grey-10" class="separator" inset />

    <div ref="quizListingsContainer" class="quiz-listings-wrapper">
      <QuizListings
          :quizzes="paginatedQuizzes"
          :current-page="currentPage"
          :total-pages="totalPages"
          :quiz-images="quizMedia.images"
          :quiz-colors="quizMedia.colors"
          @update:current-page="handlePageChange"
          @play="handlePlay"
      />
    </div>
  </main>
</template>

<style scoped lang="sass">
.article-container
  width: 99%
  padding: 10px
  margin: 5px

.separator
  margin-top: 20px

.quiz-listings-wrapper
  width: 100%
  height: 333px
  overflow: hidden
</style>