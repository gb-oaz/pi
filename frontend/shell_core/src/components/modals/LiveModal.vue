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
const confirmEndDialog = ref(false)
const confirmEndInput = ref('')

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

const currentPosition = computed(() => currentLive.value?.teacher?.control?.currentPosition ?? 0)

const liveKeyDisplay = computed(() => {
  const teacher = currentLive.value?.teacher
  if (!teacher?.login || !teacher?.code) return ''
  return `${teacher.login}#${teacher.code}`
})

const lobbyList = computed(() => currentLive.value?.lobby || [])

let lobbyTimeout: ReturnType<typeof setTimeout> | null = null

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

function confirmEnd() {
  confirmEndDialog.value = true
  confirmEndInput.value = ''
}

function cancelEnd() {
  confirmEndDialog.value = false
  confirmEndInput.value = ''
}

function proceedEnd() {
  confirmEndDialog.value = false
  confirmEndInput.value = ''
  endLive()
}

const myLoginCode = computed(() => {
  const { login, code } = authStore.scope || {}
  return login && code ? `${login}#${code}` : ''
})

const canConfirmEnd = computed(() => confirmEndInput.value.trim() === myLoginCode.value)

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
      <q-bar class="live-bar">
        <div class="row items-center">
          <span class="live-dot" />
          <q-badge class="live-key-badge" style="backdrop-filter: blur(2px);" :style="'background: rgba(34,34,34,0.24); color: #fff; border: 1px solid #ffe066;'" >{{ liveKeyDisplay }}</q-badge>
        </div>
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
                  <q-badge class="q-ml-sm pupil-code" color="#ffe066" text-color="#222">#{{ pupil.split('#')[1] }}</q-badge>
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
      <div v-if="isOwnerTeacher" class="live-controls-footer row items-center full-width no-wrap">
        <div class="row items-center control-group justify-end full-width">
          <q-btn
            v-if="currentPosition === 0"
            class="control-btn start-btn"
            flat
            label="Start"
            @click="startSession"
            color="grey-6"
            text-color="#2196f3" />
          <q-btn label="Prev" @click="goToPreviousPosition" :disable="currentPosition <= 1" class="control-btn prev-btn q-mr-sm" color="grey-6" text-color="#ffe066" flat />
          <q-btn label="Next" @click="goToNextPosition" :disable="currentPosition === 0" class="control-btn next-btn q-mr-sm" color="grey-6" text-color="#ffd600" flat />
          <q-btn label="End" @click="confirmEnd" class="control-btn end-btn" color="grey-6" text-color="#ff5252" flat />
        </div>
      </div>
    </q-card>
  </q-dialog>
  <q-dialog v-model="confirmEndDialog" persistent>
    <q-card class="bg-grey-10 text-white end-dialog-card">
      <q-card-section class="row items-center q-pb-none">
        <q-icon name="warning" color="red" size="md" class="q-mr-md" />
        <span class="text-h6">Confirm End Live</span>
      </q-card-section>
      <q-card-section class="q-pt-sm q-pb-md">
        <div>To confirm, type your login#code below:</div>
        <q-input
          v-model="confirmEndInput"
          color="yellow-8"
          bg-color="grey-9"
          label="login#code"
          label-color="grey-4"
          class="q-mt-md"
          dense
          outlined
          :error="confirmEndInput && !canConfirmEnd"
          :rules="[val => !val || canConfirmEnd || 'Type your login#code to confirm']"
          @keyup.enter="canConfirmEnd && proceedEnd()"
        />
        <div v-if="confirmEndInput && !canConfirmEnd" class="text-negative text-caption q-mt-xs">Type your login#code exactly to enable End.</div>
      </q-card-section>
      <q-card-actions align="right" class="q-px-md q-pb-md">
        <q-btn flat label="Cancel" color="grey-6" text-color="#fff" @click="cancelEnd" class="q-mr-sm" />
        <q-btn flat label="End" color="red-5" text-color="#fff" :disable="!canConfirmEnd" @click="proceedEnd" />
      </q-card-actions>
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
  background: #fffbe7 !important;
  color: #222 !important;
  border: 1px solid #ffe066 !important;
}
.pupil-remove-btn:hover {
  background: rgba(255, 0, 0, 0.12) !important;
}

.live-bar {
  background: linear-gradient(90deg, #232526 0%, #ffe066 100%);
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.10);
  min-height: 48px;
}
.live-dot {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #ff1744;
  margin-right: 16px;
  box-shadow: 0 0 8px 2px #ff174480;
  display: inline-block;
  animation: live-blink 1.2s infinite alternate;
}
@keyframes live-blink {
  0% { opacity: 1; box-shadow: 0 0 8px 2px #ff174480; }
  100% { opacity: 0.5; box-shadow: 0 0 2px 1px #ff174480; }
}
.live-key-badge {
  font-size: 0.97rem;
  letter-spacing: 0.5px;
  padding: 3px 10px;
  border-radius: 7px;
  font-weight: 500;
  box-shadow: none;
  margin-left: 0;
  margin-right: 0;
  background: rgba(34,34,34,0.24) !important;
  color: #fff !important;
  border: 1px solid #ffe066 !important;
  transition: background 0.2s, color 0.2s;
}

.control-group {
  background: rgba(255,255,255,0.05);
  border-radius: 14px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08);
  padding: 6px 14px;
  gap: 6px;
  width: 100%;
  justify-content: flex-end;
}
.control-btn {
  min-width: 68px;
  border-radius: 8px !important;
  font-weight: 500;
  font-size: 0.98rem;
  box-shadow: none;
  background: none !important;
  transition: background 0.2s, color 0.2s;
}
.start-btn {
  font-weight: 700;
  color: #2196f3 !important;
}
.prev-btn {
  color: #ffe066 !important;
}
.next-btn {
  color: #ffd600 !important;
}
.end-btn {
  color: #ff5252 !important;
  font-weight: 700;
}
.control-btn:disabled {
  opacity: 0.5;
  background: none !important;
  color: #aaa !important;
}

.end-dialog-card {
  min-width: 340px;
  border-radius: 14px;
  box-shadow: 0 2px 16px 0 rgba(0,0,0,0.18);
  background: #232526 !important;
  color: #fff !important;
}
</style>