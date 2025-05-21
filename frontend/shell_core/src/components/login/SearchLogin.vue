<script setup lang="ts">
import { computed, ref, watch} from 'vue'
import { debounce } from 'quasar'
import lupa from '../../assets/sidebar/lupa.svg'
import AuthModal from "../modals/AuthModal.vue"
import { useAuthStore } from '../../stores/authStore.ts'

type ButtonActions = 'signIn' | 'signUp'

const props = defineProps({ modelValue: { type: String, default: '' }})
const emit = defineEmits(['update:modelValue', 'search'])

// Refs
const searchTerm = ref(props.modelValue)
const buttonHoverStates = ref<Record<ButtonActions, boolean>>({
  signIn: false,
  signUp: false
})
const isFocused = ref(false)
const showModal = ref(false)
const modalMode = ref<ButtonActions>('signIn')

// Store
const authStore = useAuthStore()

// Computed
const isAnonymous = computed(() => authStore.isAnonymous)

// Methods
const handleSearch = debounce((value: string | number | null) => {
  emit('update:modelValue', value)
  emit('search', value)
}, 500)

const handleFocus = () => {
  isFocused.value = true
}

const handleBlur = () => {
  isFocused.value = false
}

const openModal = (action: ButtonActions) => {
  modalMode.value = action
  showModal.value = true
}

// Watchers
watch(() => props.modelValue, (newValue) => {
  searchTerm.value = newValue
})
</script>

<template>
  <section class="search-login">
    <q-input
        class="search-input"
        v-model="searchTerm"
        label="Search"
        label-color="white"
        input-class="text-white"
        standout="bg-grey-10 text-white"
        @update:model-value="handleSearch"
        @focus="handleFocus"
        @blur="handleBlur"
        :style="{
        width: isFocused ? '100%' : ''
      }"
    >
      <template v-slot:prepend>
        <q-img
            :src="lupa"
            width="25px"
            height="25px"
            class="cursor-pointer q-mx-sm"
            contain
        />
      </template>
    </q-input>

    <div class="button-group" v-if="!isFocused && isAnonymous">
      <q-btn
          class="custom-button"
          v-for="action in ['signIn', 'signUp']"
          :key="action"
          :color="buttonHoverStates[action as ButtonActions] ? 'yellow-14' : 'grey-10'"
          :text-color="buttonHoverStates[action as ButtonActions] ? 'black' : 'yellow-14'"
          :style="{
          border: buttonHoverStates[action as ButtonActions] ? 'solid 1px white' : 'none',
          height: buttonHoverStates[action as ButtonActions] ? '50px' : '40px'
        }"
          :label="action === 'signIn' ? 'Sign In' : 'Sign Up'"
          no-caps
          @mouseenter="buttonHoverStates[action as ButtonActions] = true"
          @mouseleave="buttonHoverStates[action as ButtonActions] = false"
          @click="openModal(action as ButtonActions)"
      />
    </div>
  </section>

  <AuthModal
      v-model="showModal"
      :mode="modalMode"
      @auth-success="authStore.updateScope"
  />
</template>

<style scoped lang="sass">
.search-login
  display: flex
  justify-content: space-between
  align-items: center
  gap: 25px
  position: relative

  .search-input
    transition: all 0.3s ease

  .button-group
    display: flex
    align-items: center
    gap: 8px
    transition: all 0.3s ease

  .custom-button
    width: 90px
    transition: all 0.3s ease
</style>