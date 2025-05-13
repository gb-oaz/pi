<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from "vue";
import { random } from '../utils/images/Randon'
import { useQuasar } from "quasar";

const $q = useQuasar()
const emit = defineEmits(['update:modelValue'])
const props = defineProps<{ modelValue: boolean, mode: 'signIn' | 'signUp' }>()
const modelValue = defineModel<boolean>()
const dialogPosition = computed(() => { return $q.screen.width < 720 ? 'bottom' : 'top' })
const currentBackground = ref(random.getRandomBackground())
const signInForm = ref({ login: '', code: '', password: '' })
const signUpForm = ref({ name: '', email: '', login: '', code: '', password: '', confirmPassword: '' })
const isPwd = ref(true)

let backgroundInterval: number | null = null

function handleSignIn() {
  // Handle sign-in logic here
  console.log('Sign In:', signInForm.value)
  emit('update:modelValue', false)
  clearForm()
}

function handleSignUp() {
  // Handle sign-up logic here
  console.log('Sign Up:', signUpForm.value)
  emit('update:modelValue', false)
  clearForm()
}

function clearForm() {
  signInForm.value = { login: '', code: '', password: '' }
  signUpForm.value = { name: '', email: '', login: '', code: '', password: '', confirmPassword: '' }
}

function startBackgroundRotation() {
  backgroundInterval = window.setInterval(() => {
    currentBackground.value = random.getRandomBackground()
  }, 5000) // Troca a cada 5 segundos
}

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
    <q-card style="min-width: 400px; max-width: 400px" :class="`bg-grey-10`">
      <q-parallax
          :height="80"
          :speed="0.5"
          :class="`bg-${props.mode === 'signIn'
          ? random.getFormStatusColor(signInForm, ['login', 'code', 'password'])
          : random.getFormStatusColor(signUpForm, ['name', 'email', 'login', 'code', 'password', 'confirmPassword'])}`"
      >
        <template v-slot:media>
          <transition name="fade" mode="out-in">
            <q-img :key="currentBackground" :src="currentBackground" style="transition: opacity 2s ease-in-out;"/>
          </transition>
        </template>
      </q-parallax>

      <q-card-section v-if="props.mode === 'signIn'">
        <div class="q-gutter-md">
          <h2 class="text-h6" align="right">Sign In</h2>
          <div class="row justify-between items-center" style="gap: 8px">
            <div class="col-6">
              <q-input
                  v-model="signInForm.login"
                  :rules="[val => val.length >= 8 || 'Min 8 characters', val => val.length <= 15 || 'Max 15 characters', val => /^[^a-z]+$/.test(val) || 'All letters must be uppercase']"
                  label="Login" color="yellow-14" outlined dense
              />
            </div>
            #
            <div class="col-5">
              <q-input v-model="signInForm.code" :rules="[val => /^\d{6}$/.test(val) || 'Must be exactly 6 digits']" label="Code" color="yellow-14" mask="######" outlined dense/>
            </div>
          </div>
          <q-input v-model="signInForm.password"
                   :rules="[
                      val => val.length >= 15 || 'Min 15 characters',
                      val => val.length <= 25 || 'Max 25 characters',
                      val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                        'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)'
                    ]"
                   :type="isPwd ? 'password' : 'text'" label="Password" color="yellow-14" outlined dense>
            <template v-slot:append>
              <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwd = !isPwd"/>
            </template>
          </q-input>
          <q-btn @click="handleSignIn" label="Sign In" color="primary"/>
        </div>

      </q-card-section>


      <q-card-section v-else>
        <div class="q-gutter-md">
          <h2 class="text-h6" align="right">Sign Up</h2>
          <q-input v-model="signUpForm.name"
                   :rules="[
                      val => !!val || 'Name is required',
                      val => val.length >= 3 || 'Min 3 characters',
                      val => val.length <= 40 || 'Max 40 characters',
                      val => /^[a-zA-ZÀ-ÿ\s]{3,40}$/.test(val) || 'Only letters and spaces are allowed'
                    ]"
                   label="Name" color="yellow-14" outlined dense/>
          <q-input v-model="signUpForm.email" :rules="[val => !!val || 'Email is required', val => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(val) || 'Invalid email format']" label="Email" color="yellow-14" outlined dense/>
          <div class="row justify-between items-center" style="gap: 8px">
            <div class="col-6">
              <q-input
                  v-model="signUpForm.login"
                  :rules="[val => val.length >= 8 || 'Min 8 characters', val => val.length <= 15 || 'Max 15 characters', val => /^[^a-z]+$/.test(val) || 'All letters must be uppercase']"
                  label="Login" color="yellow-14" outlined dense
              />
            </div>
            #
            <div class="col-5">
              <q-input v-model="signUpForm.code" :rules="[val => /^\d{6}$/.test(val) || 'Must be exactly 6 digits']" label="Code" color="yellow-14" mask="######" outlined dense/>
            </div>
          </div>
          <q-input v-model="signUpForm.password"
                   :rules="[
                      val => val.length >= 15 || 'Min 15 characters',
                      val => val.length <= 25 || 'Max 25 characters',
                      val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                        'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)'
                    ]"
                   :type="isPwd ? 'password' : 'text'" label="Password" color="yellow-14" outlined dense>
            <template v-slot:append>
              <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwd = !isPwd"/>
            </template>
          </q-input>
          <q-input v-model="signUpForm.confirmPassword"
                   :rules="[
                      val => val.length >= 15 || 'Min 15 characters',
                      val => val.length <= 25 || 'Max 25 characters',
                      val => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$/.test(val) ||
                        'Must contain at least 1 lowercase, 1 uppercase, 1 number, 1 special (!?@#$%&)'
                    ]"
                   :type="isPwd ? 'password' : 'text'" label="Confirm Password" color="yellow-14" outlined dense>
            <template v-slot:append>
              <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer" @click="isPwd = !isPwd"/>
            </template>
          </q-input>
          <q-btn @click="handleSignUp" label="Sign Up" color="primary" />
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