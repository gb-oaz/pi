<script setup lang="ts">
import { ref, onUnmounted, onMounted, watch } from 'vue'
import { useQuasar } from 'quasar'
import { liveStore } from '../../stores/liveStore'
import { LiveApi } from '../../services/live/LiveApi'
import { computed } from 'vue'
import { useAuthStore } from '../../stores/authStore'

const $q = useQuasar()
const dialog = ref(false)
const quizKey = ref('')
const sidebarVisible = ref(true)

const liveApi = new LiveApi()
const authStore = useAuthStore()
computed(() => {
  const { login, code } = authStore.scope || {}
  return !!login && !!code
});

const currentLive = computed(() => liveStore.getLive())
const isOwnerTeacher = computed(() => {
  const { login, code, scope } = authStore.scope || {}
  const teacher = currentLive.value?.teacher
  return (
      scope.includes('TEACHER') &&
      teacher &&
      login === teacher.login &&
      code === teacher.code
  )
})

const lobbyList = computed(() => currentLive.value?.lobby || [])

let lobbyTimeout: ReturnType<typeof setTimeout> | null = null

watch(lobbyList, (newLobby) => {
  const { login, code, scope } = authStore.scope || {}
  // Só valida para quem NÃO é TEACHER
  if (scope && scope.includes('TEACHER')) return
  if (!login || !code) return
  if (lobbyTimeout) clearTimeout(lobbyTimeout)
  lobbyTimeout = setTimeout(() => {
    const userKey = `${login}#${code}`
    if (!newLobby.includes(userKey)) {
      close()
    }
  }, 5000)
})

async function removePupil(pupil: string) {
  const teacher = currentLive.value?.teacher
  if (!teacher) return
  const liveKey = `LIVE${teacher.login}CODE${teacher.code}`
  // Separar login e code
  const [login, code] = pupil.split('#')
  if (!login || !code) return
  await liveApi.removePupilToLobby(login, code, liveKey)
}

let eventSource: EventSource | null = null

async function open(key: string) {
  quizKey.value = key
  const { login, code, scope } = authStore.scope || {}
  let live = liveStore.getLive()
  const teacher = live?.teacher
  // Se for o professor owner e não houver live no store, cria a live
  if (scope && scope.includes('TEACHER')) {
    if (!live || !teacher || login !== teacher.login || code !== teacher.code) {
      live = await liveApi.createLive(key)
      if (live && live.teacher) {
        liveStore.createLive(live)
      }
    }
  }
  dialog.value = true
  await startLiveStream()
}

async function startLiveStream() {
  stopLiveStream()
  // Sempre usa o teacher da live para montar a chave
  const teacher = currentLive.value?.teacher
  if (!teacher?.login || !teacher?.code) return
  const liveKey = `LIVE${teacher.login}CODE${teacher.code}`
  eventSource = await liveApi.getLiveStream(
    liveKey,
    (data) => {
      liveStore.createLive(data)
    },
    (error) => {
      // Opcional: notificar erro
    }
  )
}

function close() {
  dialog.value = false
  stopLiveStream()
  liveStore.removeLive()
}

function stopLiveStream() {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
}

async function endLive() {
  const { login: loginTeacher, code: codeTeacher } = authStore.scope || {}
  let key = ''
  if (loginTeacher && codeTeacher) {
    key = `LIVE${loginTeacher}CODE${codeTeacher}`
  }
  if (key) {
    await liveApi.endLive(key)
    liveStore.removeLive()
    dialog.value = false
    $q.notify({ message: 'Live finalizada!', color: 'positive' })
  }
}

// Funções de navegação do professor
async function goToNextPosition() {
  const { login, code } = authStore.scope || {}
  if (!login || !code) return
  const liveKey = `LIVE${login}CODE${code}`
  await liveApi.nextPosition(liveKey)
}

async function goToPreviousPosition() {
  const { login, code } = authStore.scope || {}
  if (!login || !code) return
  const liveKey = `LIVE${login}CODE${code}`
  await liveApi.previousPosition(liveKey)
}

async function startSession() {
  await goToNextPosition()
}

const currentPosition = computed(() => currentLive.value?.teacher?.control?.currentPosition ?? 0)

onMounted(() => {
  // Se a live estiver finalizada, limpa o store imediatamente
  const live = liveStore.getLive()
  if (live && live.status === 'COMPLETED') {
    liveStore.removeLive()
  }
})

onUnmounted(() => {
  stopLiveStream()
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
      maximized
  >
    <q-card class="bg-grey-10 text-white live-modal-card">
      <q-bar>
        <div>Live Quiz: {{ quizKey }}</div>
        <q-space />
        <q-btn dense flat icon="close" @click="close">
          <q-tooltip class="bg-white text-primary">Close</q-tooltip>
        </q-btn>
      </q-bar>

      <div class="live-modal-content-row">
        <div class="main-content-empty">
          <div v-if="currentPosition === 0" class="centered-message">
            <div class="text-h3 text-weight-bold q-mb-md">Aguardando o professor iniciar a sessão...</div>
            <div class="text-subtitle1">Assim que o professor iniciar, sua tela será atualizada automaticamente.</div>
          </div>
          <q-btn v-if="isOwnerTeacher && !sidebarVisible" flat dense icon="chevron_right" @click="sidebarVisible = true" class="show-sidebar-btn" />
        </div>
        <aside v-if="isOwnerTeacher && sidebarVisible" class="sidebar">
          <div class="row items-center justify-between full-width q-mb-md">
            <div class="text-h6">Live Monitor</div>
            <q-btn flat dense icon="close" @click="sidebarVisible = false" class="hide-sidebar-btn" />
          </div>
          <div class="q-mb-md text-subtitle2 text-weight-bold">Pupils</div>
          <q-list class="pupil-list">
            <q-item v-for="pupil in lobbyList" :key="pupil" class="pupil-item q-pa-none">
              <q-item-section avatar>
                <div class="pupil-avatar">{{ pupil.split('#')[0].slice(0,3).toUpperCase() }}</div>
              </q-item-section>
              <q-item-section>
                <div class="row items-center">
                  <span class="pupil-login">{{ pupil.split('#')[0] }}</span>
                  <q-badge class="q-ml-sm pupil-code" color="yellow-14" text-color="black">#{{ pupil.split('#')[1] }}</q-badge>
                </div>
              </q-item-section>
              <q-item-section side>
                <q-btn flat dense icon="person_remove" color="negative" @click="removePupil(pupil)" class="pupil-remove-btn" />
              </q-item-section>
            </q-item>
            <q-item v-if="lobbyList.length === 0" class="pupil-item q-pa-none">
              <q-item-section>Nenhum aluno presente</q-item-section>
            </q-item>
          </q-list>
        </aside>
        <template v-else-if="!isOwnerTeacher">
          <div v-if="currentPosition === 0" class="centered-message">
            <div class="text-h3 text-weight-bold q-mb-md">Aguardando o professor iniciar a sessão...</div>
            <div class="text-subtitle1">Assim que o professor iniciar, sua tela será atualizada automaticamente.</div>
          </div>
        </template>
      </div>
      <div v-if="isOwnerTeacher" class="live-controls-footer row items-center justify-between full-width no-wrap">
        <div>
          <q-btn v-if="currentPosition === 0" color="yellow-14" text-color="black" label="Start Live Session" @click="startSession" />
        </div>
        <div class="row items-center justify-end no-wrap">
          <q-btn color="grey-10" text-color="white" label="Anterior" @click="goToPreviousPosition" :disable="currentPosition <= 1" class="q-mr-sm" />
          <q-btn color="yellow-14" text-color="black" label="Próximo" @click="goToNextPosition" :disable="currentPosition === 0" class="q-mr-sm" />
          <q-btn color="negative" text-color="white" label="Finalizar Live" @click="endLive" />
        </div>
      </div>
    </q-card>
  </q-dialog>
</template>

<style scoped>
.live-modal-card {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  max-width: 100vw;
  max-height: 100vh;
  border-radius: 0;
}

.live-modal-content-row {
  display: flex;
  flex-direction: row;
  width: 100vw;
  height: calc(100vh - 80px); /* Ajuste para não cobrir o rodapé */
  position: relative;
}

.main-content-empty {
  flex: 1 1 0;
  min-height: 60vh;
  position: relative;
}

.sidebar {
  width: 420px;
  background: #222;
  color: #fff;
  padding: 24px 16px;
  border-left: 1px solid #444;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  min-height: 100%;
  box-sizing: border-box;
}

.centered-message {
  min-height: 70vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #ffd600;
  text-align: center;
  position: absolute;
  left: 0;
  top: 0;
}

.live-controls-footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background: #222;
  padding: 16px 32px;
  z-index: 100;
  border-top: 1px solid #444;
}

.show-sidebar-btn {
  position: absolute;
  right: 0;
  top: 16px;
  z-index: 10;
  background: #222;
  color: #ffd600;
  border-radius: 50%;
}

.hide-sidebar-btn {
  margin-left: 8px;
  color: #ffd600;
}

.pupil-list {
  padding: 0 !important;
  margin: 0 !important;
}

.sidebar .pupil-list {
  width: 100% !important;
  max-width: 100% !important;
  min-width: 100% !important;
  padding: 0 !important;
  margin: 0 !important;
}

.sidebar .q-item,
.sidebar .pupil-item {
  width: 100% !important;
  max-width: 100% !important;
  min-width: 100% !important;
  box-sizing: border-box;
  margin: 0 0 12px 0 !important;
  padding: 12px 8px !important;
  border-radius: 10px;
  background: rgba(255,255,255,0.04);
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.07);
  display: flex;
  align-items: center;
  transition: box-shadow 0.2s;
  border: none !important;
}

.sidebar .q-item__section {
  min-width: 0;
}

.pupil-item {
  width: 100% !important;
  margin: 0 0 12px 0;
  padding: 12px 8px;
  border-radius: 10px;
  background: rgba(255,255,255,0.04);
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.07);
  display: flex;
  align-items: center;
  transition: box-shadow 0.2s;
  border: none !important;
}
.pupil-item:hover {
  box-shadow: 0 4px 16px 0 rgba(255, 214, 0, 0.13);
}
.pupil-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: #ffd600;
  color: #222;
  font-weight: bold;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 6px 0 rgba(0,0,0,0.10);
}
.pupil-login {
  font-weight: 600;
  font-size: 1.1rem;
  color: #fff;
}
.pupil-code {
  font-size: 0.95rem;
  border-radius: 6px;
  font-weight: 500;
}
.pupil-remove-btn:hover {
  background: rgba(255, 0, 0, 0.12) !important;
}
</style>