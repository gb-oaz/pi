<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import CreateQuizModal from '../modals/CreateQuizModal.vue'
import LiveModal from '../modals/LiveModal.vue'
import logo from '../../assets/by_gw-q.png'
import logout from '../../assets/sidebar/logout.svg'
import lupa from '../../assets/sidebar/lupa.svg'
import sinal from '../../assets/sidebar/sinal.svg'
import home from '../../assets/sidebar/home.svg'
import profile from '../../assets/sidebar/profile.svg'
import groups from '../../assets/sidebar/groups.svg'
import dashboard from '../../assets/sidebar/dashboard.svg'
import { AuthApi } from "../../services/auth/AuthApi.ts";
import { useAuthStore } from '../../stores/authStore.ts'
import { liveStore } from '../../stores/liveStore'
import { LiveApi } from '../../services/live/LiveApi'
import { useQuasar } from 'quasar'
import type { Ref } from 'vue'
import type { ILive } from '../../services/live/types/ILive'

const router = useRouter()
const authStore = useAuthStore()
const authApi = new AuthApi()
const liveApi = new LiveApi()
const $q = useQuasar()
const isMobile = computed(() => windowWidth.value <= 720)
const showSidebar = ref(false)
const searchLive = ref<string>('')
const windowWidth = ref<number>(window.innerWidth)
const selectedButton = ref<string>('home')
const createQuizModalRef = ref<InstanceType<typeof CreateQuizModal> | null>(null)
const liveModalRef = ref<InstanceType<typeof LiveModal> | null>(null)

async function handleSearchLive(): Promise<void> {
  if (!searchLive.value) {
    $q.notify({ message: 'Digite login#code do professor!', color: 'warning' })
    return
  }
  try {
    const liveKeyMasked = maskLiveKey(searchLive.value)
    const live: ILive = await liveApi.getLive(liveKeyMasked)
    if (live && live.key) {
      liveStore.createLive(live)
      // Registrar entrada no lobby se não for o professor owner
      const { login, code, scope } = authStore.scope || {}
      const teacher = live.teacher
      if (
          teacher &&
          !(scope && scope.includes('TEACHER') && login === teacher.login && code === teacher.code)
      ) {
        const liveKey = maskLiveKey(`${teacher.login}#${teacher.code}`)
        await liveApi.addPupilToLobby(liveKey)
      }
      liveModalRef.value?.open(live.key)
      $q.notify({ message: 'Live carregada!', color: 'positive' })
    } else {
      $q.notify({ message: 'Live não encontrada.', color: 'negative' })
    }
  } catch (e) {
    $q.notify({ message: 'Erro ao buscar live.', color: 'negative' })
  }
}

async function endSession(): Promise<void> {
  await authApi.signOut()
  authStore.updateScope()
}

function hasPermission(requiredRole: string): boolean {
  return authStore.hasAnyRole(
    requiredRole === 'home' ? ['ANONYMOUS', 'STUDENT', 'TEACHER'] :
          requiredRole === 'profile' ? ['STUDENT', 'TEACHER'] :
          requiredRole === 'groups' ? ['STUDENT', 'TEACHER'] :
          ['TEACHER'] // dashboard
  )
}

function navigateTo(routeName: string): void {
  selectedButton.value = routeName
  router.push({ name: routeName })
}

function updateWidth(): void {
  windowWidth.value = window.innerWidth
}

function openQuizModal(): void {
  createQuizModalRef.value?.open()
  showSidebar.value = false
}

function maskLiveKey(input: string): string {
  // Aceita login#code e retorna LIVEloginCODEcode
  if (!input || !input.includes('#')) return input
  const [login, code] = input.split('#')
  return `LIVE${login}CODE${code}`
}

onMounted(() => {
  window.addEventListener('resize', updateWidth)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateWidth)
})
</script>

<template>
  <!-- Versão desktop (sidebar normal) - só aparece acima de 720px -->
  <aside
      v-show="!isMobile"
      class="sidebar-container column items-start justify-between full-height no-wrap"
  >
    <!-- Conteúdo da sidebar... -->
    <header class="full-width">
      <q-img :src="logo" style="width: 45px; height: 45px" class="q-mb-md" contain />

      <q-input v-model="searchLive" color="yellow" bg-color="grey-14" label="Live" label-color="white" class="full-width" input-class="text-white" rounded standout bottom-slots counter @keyup.enter="handleSearchLive">
        <template v-slot:prepend>
          <q-img :src="sinal" style="width: 15px; height: 11px; margin: 7px" contain/>
        </template>
        <template v-slot:append>
          <q-img :src="lupa" style="width: 20px; height: 20px; margin: 7px" @click="handleSearchLive" class="cursor-pointer" contain/>
        </template>
      </q-input>

      <div class="text-white text-caption q-mb-sm">Menu</div>

      <q-btn
          v-if="hasPermission('home')"
          align="between"
          :color="selectedButton === 'home' ? 'yellow-14' : 'grey-10'"
          class="full-width"
          :text-color="selectedButton === 'home' ? 'black' : 'white'"
          size="md"
          label="Home"
          no-caps
          @click="navigateTo('home')"
      >
        <q-img
            :src="home"
            contain
            style="width: 17px; height: 17px;"
            :style="{ filter: selectedButton === 'home' ? 'invert(0%)' : 'invert(100%)' }"
        />
      </q-btn>

      <q-btn
          v-if="hasPermission('profile')"
          align="between"
          :color="selectedButton === 'profile' ? 'yellow-14' : 'grey-10'"
          class="full-width"
          :text-color="selectedButton === 'profile' ? 'black' : 'white'"
          size="md"
          label="Profile"
          no-caps
          @click="navigateTo('profile')"
      >
        <q-img
            :src="profile"
            contain
            style="width: 17px; height: 16px;"
            :style="{ filter: selectedButton === 'profile' ? 'invert(0%)' : 'invert(100%)' }"
        />
      </q-btn>

      <q-btn
          v-if="hasPermission('groups')"
          align="between"
          :color="selectedButton === 'groups' ? 'yellow-14' : 'grey-10'"
          class="full-width"
          :text-color="selectedButton === 'groups' ? 'black' : 'white'"
          size="md"
          label="Groups"
          no-caps
          @click="navigateTo('groups')"
      >
        <q-img
            :src="groups"
            contain
            style="width: 17px; height: 16px;"
            :style="{ filter: selectedButton === 'groups' ? 'invert(0%)' : 'invert(100%)' }"
        />
      </q-btn>

      <q-btn
          v-if="hasPermission('dashboard')"
          align="between"
          :color="selectedButton === 'dashboard' ? 'yellow-14' : 'grey-10'"
          class="full-width"
          :text-color="selectedButton === 'dashboard' ? 'black' : 'white'"
          size="md"
          label="Dashboard"
          no-caps
          @click="navigateTo('dashboard')"
      >
        <q-img
            :src="dashboard"
            contain
            style="width: 16px; height: 15px;"
            :style="{ filter: selectedButton === 'dashboard' ? 'invert(0%)' : 'invert(100%)' }"
        />
      </q-btn>

      <q-separator color="yellow-14" class="separator" inset />

      <div v-if="hasPermission('teacher-group')" class="accordion-teacher">
        <q-list bordered>
          <q-expansion-item group="teacher-group" label="Quiz Actions" header-class="text-white">
            <div style="padding: 10px">
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Flashcard" no-caps>
                <strong class="text-caption text-weight-thin">(Create cards for study)</strong>
              </q-btn>
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Quiz" no-caps @click="openQuizModal">
                <strong class="text-caption text-weight-thin">(Create lesson for home work)</strong>
              </q-btn>
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Live class" no-caps>
                <strong class="text-caption text-weight-thin">(Create a live action class)</strong>
              </q-btn>
            </div>
          </q-expansion-item>

          <q-separator />

          <q-expansion-item group="teacher-group" label="Group Actions" header-class="text-white">
            <div style="padding: 10px">
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Study" no-caps>
                <strong class="text-caption text-weight-thin">(Create a small group 1 to 3)</strong>
              </q-btn>
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Team" no-caps>
                <strong class="text-caption text-weight-thin">(Create a medium group 5 to 10)</strong>
              </q-btn>
              <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Class room" no-caps>
                <strong class="text-caption text-weight-thin">(Create a big group 10 +)</strong>
              </q-btn>
            </div>
          </q-expansion-item>

        </q-list>
      </div>


    </header>
    <footer class="full-width">
      <q-btn align="between" class="full-width" style="border-radius: 4px; margin-bottom: 0" caps fab>
        <div class="row items-center justify-between no-wrap full-width">
          <div class="column items-start justify-between no-wrap">
            <span class="text-subtitle1 text-weight-thin" style="color: goldenrod">{{ authStore.scope?.login }}</span>
            <span class="text-subtitle2 text-weight-thin" style="color: white">
              CODE: <span class="text-caption text-weight-thin" style="color: goldenrod">{{ authStore.scope?.code }}</span>
            </span>
            <span class="text-subtitle2 text-weight-thin" style="color: white">
              ACCOUNT: <span class="text-caption text-weight-thin" style="color: goldenrod">{{ authStore.scope?.scope[0] }}</span>
            </span>
          </div>
          <q-btn round @click="endSession" v-if="!authStore.scope?.scope.includes('ANONYMOUS')" style="margin-bottom: 0">
            <q-img :src="logout" contain style="width: 30px; height: 30px"/>
            <q-tooltip anchor="top middle" self="bottom middle" :offset="[10, 10]" class="bg-amber text-black" transition-show="scale" transition-hide="scale">
              <strong>Logout</strong>
            </q-tooltip>
          </q-btn>
        </div>
      </q-btn>
    </footer>
  </aside>

  <!-- Versão mobile (apenas FAB) -->
  <q-fab
      v-if="isMobile"
      v-model="showSidebar"
      color="yellow-14"
      icon="menu"
      text-color="black"
      direction="up"
      vertical-actions-align="right"
      class="fab-button"
  />

  <!-- Dialog com o conteúdo da sidebar -->
  <q-dialog v-model="showSidebar" full-height>
    <q-card class="sidebar-dialog">
      <q-card-section class="column items-start justify-between full-height no-wrap">
        <header class="full-width" style="margin-bottom: 10px">
          <q-img :src="logo" style="width: 45px; height: 45px" class="q-mb-md" contain />

          <q-input v-model="searchLive" color="yellow" bg-color="grey-14" label="Live" label-color="white" class="full-width" input-class="text-white" rounded standout bottom-slots counter @keyup.enter="handleSearchLive">
            <template v-slot:prepend>
              <q-img :src="sinal" style="width: 15px; height: 11px; margin: 7px" contain/>
            </template>
            <template v-slot:append>
              <q-img :src="lupa" style="width: 20px; height: 20px; margin: 7px" @click="handleSearchLive" class="cursor-pointer" contain/>
            </template>
          </q-input>

          <div class="text-white text-caption q-mb-sm">Menu</div>

          <q-btn
              v-if="hasPermission('home')"
              align="between"
              :color="selectedButton === 'home' ? 'yellow-14' : 'grey-10'"
              class="full-width"
              :text-color="selectedButton === 'home' ? 'black' : 'white'"
              size="md"
              label="Home"
              no-caps
              @click="navigateTo('home'); showSidebar = false"
          >
            <q-img
                :src="home"
                contain
                style="width: 17px; height: 17px;"
                :style="{ filter: selectedButton === 'home' ? 'invert(0%)' : 'invert(100%)' }"
            />
          </q-btn>

          <q-btn
              v-if="hasPermission('profile')"
              align="between"
              :color="selectedButton === 'profile' ? 'yellow-14' : 'grey-10'"
              class="full-width"
              :text-color="selectedButton === 'profile' ? 'black' : 'white'"
              size="md"
              label="Profile"
              no-caps
              @click="navigateTo('profile'); showSidebar = false"
          >
            <q-img
                :src="profile"
                contain
                style="width: 17px; height: 16px;"
                :style="{ filter: selectedButton === 'profile' ? 'invert(0%)' : 'invert(100%)' }"
            />
          </q-btn>

          <q-btn
              v-if="hasPermission('groups')"
              align="between"
              :color="selectedButton === 'groups' ? 'yellow-14' : 'grey-10'"
              class="full-width"
              :text-color="selectedButton === 'groups' ? 'black' : 'white'"
              size="md"
              label="Groups"
              no-caps
              @click="navigateTo('groups'); showSidebar = false"
          >
            <q-img
                :src="groups"
                contain
                style="width: 17px; height: 16px;"
                :style="{ filter: selectedButton === 'groups' ? 'invert(0%)' : 'invert(100%)' }"
            />
          </q-btn>

          <q-btn
              v-if="hasPermission('dashboard')"
              align="between"
              :color="selectedButton === 'dashboard' ? 'yellow-14' : 'grey-10'"
              class="full-width"
              :text-color="selectedButton === 'dashboard' ? 'black' : 'white'"
              size="md"
              label="Dashboard"
              no-caps
              @click="navigateTo('dashboard'); showSidebar = false"
          >
            <q-img
                :src="dashboard"
                contain
                style="width: 16px; height: 15px;"
                :style="{ filter: selectedButton === 'dashboard' ? 'invert(0%)' : 'invert(100%)' }"
            />
          </q-btn>

          <q-separator color="yellow-14" class="separator" inset />

          <div v-if="hasPermission('teacher-group')" class="accordion-teacher">
            <q-list bordered>
              <q-expansion-item group="teacher-group" label="Quiz Actions" header-class="text-white">
                <div style="padding: 10px">
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Flashcard" no-caps>
                    <strong class="text-caption text-weight-thin">(Create cards for study)</strong>
                  </q-btn>
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Quiz" no-caps @click="openQuizModal">
                    <strong class="text-caption text-weight-thin">(Create lesson for home work)</strong>
                  </q-btn>
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Live class" no-caps>
                    <strong class="text-caption text-weight-thin">(Create a live action class)</strong>
                  </q-btn>
                </div>
              </q-expansion-item>

              <q-separator />

              <q-expansion-item group="teacher-group" label="Group Actions" header-class="text-white">
                <div style="padding: 10px">
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Study" no-caps>
                    <strong class="text-caption text-weight-thin">(Create a small group 1 to 3)</strong>
                  </q-btn>
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Team" no-caps>
                    <strong class="text-caption text-weight-thin">(Create a medium group 5 to 10)</strong>
                  </q-btn>
                  <q-btn class="full-width text-justify" size="md" color="gray-10" text-color="white" label="Class room" no-caps>
                    <strong class="text-caption text-weight-thin">(Create a big group 10 +)</strong>
                  </q-btn>
                </div>
              </q-expansion-item>

            </q-list>
          </div>

        </header>
        <footer class="full-width">
          <q-btn align="between" class="full-width" style="border-radius: 4px; margin-bottom: 10px" caps fab @click="showSidebar = false">
            <div class="row items-center justify-between no-wrap full-width">
              <div class="column items-start justify-between no-wrap">
                <span class="text-subtitle1 text-weight-thin" style="color: goldenrod">{{ authStore.scope?.login }}</span>
                <span class="text-subtitle2 text-weight-thin" style="color: white">
                  CODE: <span class="text-caption text-weight-thin" style="color: goldenrod">{{ authStore.scope?.code }}</span>
                </span>
                <span class="text-subtitle2 text-weight-thin" style="color: white">
                  ACCOUNT: <span class="text-caption text-weight-thin" style="color: goldenrod">{{ authStore.scope?.scope[0] }}</span>
                </span>
              </div>
              <q-btn round @click="endSession"  v-if="!authStore.scope?.scope.includes('ANONYMOUS')" style="margin-bottom: 0">
                <q-img :src="logout" contain style="width: 30px; height: 30px"/>
                <q-tooltip anchor="top middle" self="bottom middle" :offset="[10, 10]" class="bg-amber text-black" transition-show="scale" transition-hide="scale">
                  <strong>Logout</strong>
                </q-tooltip>
              </q-btn>
            </div>
          </q-btn>
        </footer>
      </q-card-section>
    </q-card>
  </q-dialog>

  <CreateQuizModal ref="createQuizModalRef" />
  <LiveModal ref="liveModalRef" />
</template>

<style scoped lang="sass">
.sidebar-container
  background-color: #242424
  width: 280px
  padding: 20px
  margin: 15px
  border-radius: 5px
  display: none

  &:not(.mobile)
    display: flex

.fab-button
  position: fixed
  bottom: 20px
  right: 20px
  z-index: 1000

.sidebar-dialog
  background-color: #242424
  width: 280px
  border-radius: 5px

.q-btn
  margin-bottom: 10px

.separator
  margin-top: 10px
  margin-bottom: 15px

.accordion-teacher
  margin-bottom: 10px
  width: 100%

body.mobile .sidebar-container
  display: none !important
</style>