<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from "vue";
import { random } from '../utils/images/Randon'
import { useQuasar } from "quasar";
import { AuthApi } from "../services/auth/AuthApi.ts";
import { UserApi } from "../services/user/UserApi.ts";
import { useAuthStore } from "../stores/authStore";

const $q = useQuasar()
const authApi = new AuthApi()
const userApi = new UserApi();
const authStore = useAuthStore()

const emit = defineEmits(['update:modelValue', 'auth-success'])
const props = defineProps<{
  modelValue: boolean,
  mode: 'signIn' | 'signUp'
}>()

const modelValue = defineModel<boolean>()
const dialogPosition = computed(() => $q.screen.width < 720 ? 'bottom' : 'top')
const currentBackground = ref(random.getRandomBackground())
const isPwd = ref(true)

// Form data
const signInForm = ref({
  login: '',
  code: '',
  password: ''
})

const signUpForm = ref({
  name: '',
  email: '',
  login: '',
  code: '',
  password: '',
  confirmPassword: ''
})

// Form validation
const isSignInValid = computed(() => {
  const { login, code, password } = signInForm.value
  return (
      login.length >= 8 &&
      login.length <= 15 &&
      /^[^a-z]+$/.test(login) &&
      /^\d{6}$/.test(code) &&
      password.length >= 15 &&
      password.length <= 25 &&
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(password)
  )
})

const isSignUpValid = computed(() => {
  const { name, email, login, code, password, confirmPassword } = signUpForm.value
  return (
      name.length >= 3 &&
      name.length <= 40 &&
      /^[a-zA-ZÀ-ÿ\s]{3,40}$/.test(name) &&
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email) &&
      login.length >= 8 &&
      login.length <= 15 &&
      /^[^a-z]+$/.test(login) &&
      /^\d{6}$/.test(code) &&
      password.length >= 15 &&
      password.length <= 25 &&
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(password) &&
      password === confirmPassword
  )
})

// Background rotation
let backgroundInterval: number | null = null

const startBackgroundRotation = () => {
  backgroundInterval = window.setInterval(() => {
    currentBackground.value = random.getRandomBackground()
  }, 5000)
}

// Auth handlers
const handleSignIn = async () => {
  try {
    const { login, code, password } = signInForm.value
    await authApi.signIn(login, code, password)
    authStore.updateScope()

    if (authStore.isAuthenticated) {
      $q.notify({
        type: 'positive',
        message: 'User successfully logged in',
      })
      emit('auth-success')
      modelValue.value = false
    } else {
      $q.notify({
        type: 'warning',
        message: 'Login code and password do not match'
      })
    }
  } catch (error) {
    console.error('Sign In Error:', error)
    $q.notify({
      type: 'negative',
      message: 'Login failed. Please check your credentials'
    })
  } finally {
    clearForm()
  }
}

const handleSignUp = async () => {
  if (signUpForm.value.password !== signUpForm.value.confirmPassword) {
    $q.notify({
      type: 'warning',
      message: 'Passwords do not match'
    })
    return
  }

  try {
    const { name, email, login, code, password } = signUpForm.value
    const result = await userApi.createStudent(name, email, login, code, password)

    if (result) {
      $q.notify({
        type: 'positive',
        message: 'User created successfully',
      })
      emit('auth-success')
      modelValue.value = false
    } else {
      $q.notify({
        type: 'warning',
        message: 'Error creating user. Please try different login or code'
      })
    }
  } catch (error) {
    console.error('Sign Up Error:', error)
    $q.notify({
      type: 'negative',
      message: 'Registration failed. Please try again'
    })
  } finally {
    clearForm()
  }
}

const clearForm = () => {
  signInForm.value = { login: '', code: '', password: '' }
  signUpForm.value = { name: '', email: '', login: '', code: '', password: '', confirmPassword: '' }
}

// Lifecycle hooks
onMounted(() => {
  startBackgroundRotation()
})

onUnmounted(() => {
  if (backgroundInterval) {
    clearInterval(backgroundInterval)
  }
})
</script>

<template>
  <q-dialog v-model="modelValue" :position="dialogPosition">
    <q-card class="bg-grey-10" style="min-width: 400px; max-width: 400px">
      <q-parallax
          :height="80"
          :speed="0.5"
          :class="`bg-${props.mode === 'signIn'
          ? random.getFormStatusColor(signInForm, ['login', 'code', 'password'])
          : random.getFormStatusColor(signUpForm, ['name', 'email', 'login', 'code', 'password', 'confirmPassword'])}`"
      >
        <template v-slot:media>
          <transition name="fade" mode="out-in">
            <q-img
                :key="currentBackground"
                :src="currentBackground"
                style="transition: opacity 2s ease-in-out;"
            />
          </transition>
        </template>
      </q-parallax>

      <q-card-section v-if="props.mode === 'signIn'">
        <div class="q-gutter-md">
          <h2 class="text-h6 text-right">Sign In</h2>

          <div class="row justify-between items-center q-gutter-sm">
            <q-input
                class="col"
                v-model="signInForm.login"
                :rules="[
                val => val.length >= 8 || 'Min 8 characters',
                val => val.length <= 15 || 'Max 15 characters',
                val => /^[^a-z]+$/.test(val) || 'All letters must be uppercase'
              ]"
                label="Login"
                color="yellow-14"
                outlined
                dense
            />

            <span>#</span>

            <q-input
                class="col-5"
                v-model="signInForm.code"
                :rules="[val => /^\d{6}$/.test(val) || 'Must be exactly 6 digits']"
                label="Code"
                color="yellow-14"
                mask="######"
                outlined
                dense
            />
          </div>

          <q-input
              v-model="signInForm.password"
              :rules="[
              val => val.length >= 15 || 'Min 15 characters',
              val => val.length <= 25 || 'Max 25 characters',
              val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)'
            ]"
              :type="isPwd ? 'password' : 'text'"
              label="Password"
              color="yellow-14"
              outlined
              dense
          >
            <template v-slot:append>
              <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
              />
            </template>
          </q-input>

          <div class="row justify-end">
            <q-btn
                @click="handleSignIn"
                label="Sign In"
                color="yellow-14"
                outline
                :disable="!isSignInValid"
            />
          </div>
        </div>
      </q-card-section>

      <q-card-section v-else>
        <div class="q-gutter-md">
          <h2 class="text-h6 text-right">Sign Up</h2>

          <q-input
              v-model="signUpForm.name"
              :rules="[
              val => !!val || 'Name is required',
              val => val.length >= 3 || 'Min 3 characters',
              val => val.length <= 40 || 'Max 40 characters',
              val => /^[a-zA-ZÀ-ÿ\s]{3,40}$/.test(val) || 'Only letters and spaces are allowed'
            ]"
              label="Name"
              color="yellow-14"
              outlined
              dense
          />

          <q-input
              v-model="signUpForm.email"
              :rules="[
              val => !!val || 'Email is required',
              val => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(val) || 'Invalid email format'
            ]"
              label="Email"
              color="yellow-14"
              outlined
              dense
          />

          <div class="row justify-between items-center q-gutter-sm">
            <q-input
                class="col"
                v-model="signUpForm.login"
                :rules="[
                val => val.length >= 8 || 'Min 8 characters',
                val => val.length <= 15 || 'Max 15 characters',
                val => /^[^a-z]+$/.test(val) || 'All letters must be uppercase'
              ]"
                label="Login"
                color="yellow-14"
                outlined
                dense
            />

            <span>#</span>

            <q-input
                class="col-5"
                v-model="signUpForm.code"
                :rules="[val => /^\d{6}$/.test(val) || 'Must be exactly 6 digits']"
                label="Code"
                color="yellow-14"
                mask="######"
                outlined
                dense
            />
          </div>

          <q-input
              v-model="signUpForm.password"
              :rules="[
              val => val.length >= 15 || 'Min 15 characters',
              val => val.length <= 25 || 'Max 25 characters',
              val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)'
            ]"
              :type="isPwd ? 'password' : 'text'"
              label="Password"
              color="yellow-14"
              outlined
              dense
          >
            <template v-slot:append>
              <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
              />
            </template>
          </q-input>

          <q-input
              v-model="signUpForm.confirmPassword"
              :rules="[
              val => val.length >= 15 || 'Min 15 characters',
              val => val.length <= 25 || 'Max 25 characters',
              val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)',
              val => val === signUpForm.password || 'Passwords must match'
            ]"
              :type="isPwd ? 'password' : 'text'"
              label="Confirm Password"
              color="yellow-14"
              outlined
              dense
          >
            <template v-slot:append>
              <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
              />
            </template>
          </q-input>

          <div class="row justify-end">
            <q-btn
                @click="handleSignUp"
                label="Sign Up"
                color="yellow-14"
                outline
                :disable="!isSignUpValid"
            />
          </div>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<style scoped lang="sass">
.fade-enter-active, .fade-leave-active
  transition: opacity 2s ease

.fade-enter-from, .fade-leave-to
  opacity: 0
</style>