// stores/useCreateQuizStore.ts
import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import type { IQuiz } from "../services/quiz/types/IQuiz.ts"
import type { IQuizItem } from "../services/quiz/types/IQuizItem.ts"

const STORAGE_KEY = 'createQuizStore'

export const useCreateQuizStore = defineStore('createQuiz', () => {
    // Carrega do localStorage se existir
    const loadFromStorage = (): IQuiz => {
        const storedData = localStorage.getItem(STORAGE_KEY)
        return storedData ? JSON.parse(storedData) : {
            key: '',
            login: '',
            code: '',
            name: '',
            quizes: [],
            categories: []
        }
    }

    // Estado inicial
    const quiz = ref<IQuiz>(loadFromStorage())

    // Salva no localStorage sempre que houver mudanças
    const saveToStorage = () => {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(quiz.value))
    }

    // Observa mudanças no quiz e salva automaticamente
    watch(
        quiz,
        () => {
            saveToStorage()
        },
        { deep: true }
    )

    const addQuizItem = (item: IQuizItem) => {
        quiz.value.quizes.push(item)
        // O watch vai pegar essa mudança e salvar automaticamente
    }

    const updateQuizItem = (index: number, item: IQuizItem) => {
        quiz.value.quizes[index] = item
        // O watch vai pegar essa mudança e salvar automaticamente
    }

    const removeQuizItem = (index: number) => {
        quiz.value.quizes.splice(index, 1)
        // O watch vai pegar essa mudança e salvar automaticamente
    }

    // Método para limpar o localStorage quando necessário
    const clearStorage = () => {
        localStorage.removeItem(STORAGE_KEY)
        quiz.value = {
            key: '',
            login: '',
            code: '',
            name: '',
            quizes: [],
            categories: []
        }
    }

    return {
        quiz,
        addQuizItem,
        updateQuizItem,
        removeQuizItem,
        clearStorage
    }
})