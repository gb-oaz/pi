<script setup lang="ts">
import type { IQuiz } from "../../services/quiz/types/IQuiz.ts";
import { ref } from "vue";
import { useAuthStore } from "../../stores/authStore.ts";

interface Props {
  quizzes: IQuiz[];
  currentPage: number;
  totalPages: number;
  quizImages: Record<string, string>;
  quizColors: Record<string, string>;
}

const { quizzes, currentPage, totalPages, quizImages, quizColors } = defineProps<Props>();
const emit = defineEmits(['update:currentPage', 'play']);

const hoveredCard = ref<string | null>(null);
const authStore = useAuthStore();

const handlePageChange = (page: number) => emit('update:currentPage', page);
const handlePlay = (quiz: IQuiz) => emit('play', quiz);

const isTeacher = () => {
  return authStore.hasAnyRole(['TEACHER']);
};
</script>

<template>
  <section class="quiz-listings">
    <div class="quiz-listings__header">
      <h2 class="quiz-listings__title">Featured Quiz</h2>
      <q-pagination
          :model-value="currentPage"
          color="yellow-14"
          text-color="black"
          :max="totalPages"
          :max-pages="5"
          :ellipses="false"
          :boundary-numbers="false"
          @update:model-value="handlePageChange"
      />
    </div>

    <div class="quiz-listings__cards">
      <transition-group name="slide-fade">
        <template v-if="quizzes.length > 0">
          <q-card
              v-for="quiz in quizzes"
              :key="quiz.key"
              :class="`bg-${quizColors[quiz.key]}`"
              class="quiz-card"
              @mouseenter="isTeacher() ? hoveredCard = quiz.key : null"
              @mouseleave="isTeacher() ? hoveredCard = null : null"
          >
            <div v-if="hoveredCard === quiz.key && isTeacher()" class="quiz-card__overlay">
              <q-btn color="green" label="Live" @click.stop="handlePlay(quiz)" size="45px" caps round/>
            </div>

            <q-img
                :src="quizImages[quiz.key]"
                :ratio="16/9"
                class="quiz-card__image"
            />

            <q-card-section class="quiz-card__content">
              <q-badge color="dark" text-color="white" align="middle" floating>{{ quiz.quizes.length || 0 }} questions by {{ quiz.login }}</q-badge>
              <div class="quiz-card__name">{{ quiz.name }}</div>
              <div class="quiz-card__categories" v-if="quiz.categories.length > 0">Categories: {{ quiz.categories.join(', ') }}</div>
            </q-card-section>
          </q-card>
        </template>

        <q-card v-else class="quiz-card--empty">
          <q-card-section class="quiz-card__empty-state">
            <q-icon name="quiz" size="lg" color="grey-5" />
            <div class="quiz-card__empty-text">No quizzes available</div>
          </q-card-section>
        </q-card>
      </transition-group>
    </div>
  </section>
</template>

<style scoped lang="sass">
.quiz-listings
  margin-top: 20px
  width: 100%

  &__header
    display: flex
    justify-content: space-between
    align-items: center
    margin-bottom: 15px

  &__title
    color: white
    font-size: 1rem
    margin: 0

  &__cards
    display: flex
    flex-wrap: nowrap
    gap: 15px
    padding-bottom: 10px
    scrollbar-width: none
    container-type: inline-size

    &::-webkit-scrollbar
      display: none

.quiz-card
  flex: 0 0 362px
  min-width: 362px
  border-radius: 0
  transition: all 0.3s ease
  cursor: pointer
  position: relative
  height: 250px
  overflow: hidden

  @container (max-width: 400px)
    flex: 0 0 350px
    min-width: 350px
    height: 200px

  &__image
    height: 160px
    width: 100%
    object-fit: cover

    @container (max-width: 400px)
      height: 120px

  &__content
    padding: 16px
    background: rgba(33, 33, 33, 0.9)
    height: 90px
    width: 100%
    position: relative

    @container (max-width: 400px)
      height: 80px

  &__name
    font-weight: 300
    font-size: 1.25rem
    color: white
    white-space: nowrap
    overflow: hidden
    text-overflow: ellipsis

  &__categories
    font-weight: 300
    font-size: 0.75rem
    color: #bdbdbd
    margin-top: 8px
    white-space: nowrap
    overflow: hidden
    text-overflow: ellipsis

  &__overlay
    position: absolute
    top: 0
    left: 0
    width: 100%
    height: 100%
    background-color: rgba(0, 0, 0, 0.91)
    display: flex
    justify-content: center
    align-items: center
    z-index: 1
    .q-btn
      animation: pulse 2s infinite

  &--empty
    @extend .quiz-card
    background-color: #424242
    display: flex
    justify-content: center
    align-items: center

    @container (max-width: 400px)
      flex: 0 0 350px
      min-width: 350px

  &__empty-state
    display: flex
    flex-direction: column
    align-items: center
    justify-content: center
    border: none
    width: 100%

  &__empty-text
    font-size: 0.875rem
    color: #bdbdbd
    text-align: center
    margin-top: 8px

/* Animations */
.slide-fade-enter-active,
.slide-fade-leave-active
  transition: all 0.5ms ease
  position: absolute

.slide-fade-enter-from
  opacity: 0
  transform: translateX(30px)

.slide-fade-leave-to
  opacity: 0
  transform: translateX(-30px)

.slide-fade-move
  transition: transform 0.3s ease

@keyframes pulse
  0%
    transform: scale(1)
  50%
    transform: scale(1.05)
  100%
    transform: scale(1)
</style>