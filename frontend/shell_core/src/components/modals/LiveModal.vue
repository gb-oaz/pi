<script setup lang="ts">
import { ref, onUnmounted, onMounted, watch, computed } from 'vue'
import { useQuasar } from 'quasar'
import { liveStore } from '../../stores/liveStore'
import { LiveApi } from '../../services/live/LiveApi'
import { useAuthStore } from '../../stores/authStore'
import SlidePreview from "../presentations/lives/SlidePreview.vue";
import QuizPreview from "../presentations/lives/QuizPreview.vue";
import type {IQuiz} from "../../services/quiz/types/IQuiz.ts";
import type {IQuizItem} from "../../services/quiz/types/IQuizItem.ts";

const $q = useQuasar()
const liveApi = new LiveApi()
const authStore = useAuthStore()

const dialog = ref(false)
const quizKey = ref('')
const confirmEndDialog = ref(false)
const confirmEndInput = ref('')
const monitorOpen = ref(false)

const lobbyList = computed(() => currentLive.value?.lobby || [])
const canConfirmEnd = computed(() => confirmEndInput.value.trim() === myLoginCode.value)
const currentPosition = computed(() => currentLive.value?.teacher?.control?.currentPosition ?? 0)
const currentLive = computed(() => liveStore.getLive())
const isOwnerTeacher = computed(() => {
  const { login, code, scope } = authStore.scope || {}
  const teacher = currentLive.value?.teacher
  return (
    Array.isArray(scope) && scope.includes('TEACHER') &&
    teacher &&
    login === teacher.login &&
    code === teacher.code
  )
})
const myLoginCode = computed(() => {
  const { login, code } = authStore.scope || {}
  return login && code ? `${login}#${code}` : ''
})
const liveKeyDisplay = computed(() => {
  const teacher = currentLive.value?.teacher
  if (!teacher?.login || !teacher?.code) return ''
  return `${teacher.login}#${teacher.code}`
})

const quiz = computed<IQuiz | undefined>(() => currentLive.value?.quiz);

const currentItem = computed<IQuizItem | null>(() => {
  if (!quiz.value || !Array.isArray(quiz.value?.quizes)) return null;
  return quiz.value.quizes.find((item: IQuizItem) => item.position === currentPosition.value) || null;
});
const previewType = computed<string>(() => currentItem.value?.type || "");
const previewData = computed<any>(() => currentItem.value || {});

// --- SIMULAÇÃO FAKES PARA VISUALIZAÇÃO ---
// Remova este bloco quando quiser usar dados reais!
function randomLogin() {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const len = Math.floor(Math.random() * 8) + 8 // 8-15
  let login = ''
  for (let i = 0; i < len; i++) login += chars[Math.floor(Math.random()*chars.length)]
  return login
}
function randomCode() {
  return String(Math.floor(100000 + Math.random()*900000))
}
function randomFakeRow() {
  const login = randomLogin()
  const code = randomCode()
  const total = Math.floor(Math.random()*10)+1
  const score = Math.floor(Math.random()*(total+1))
  const percent = total ? Math.round((score/total)*100) : 0
  const lastHit = Math.random() > 0.4 ? true : false
  return { login, code, raw: `${login}#${code}`, score, total, percent, lastHit }
}
const monitorRows = computed(() => {
  const arr = []
  for (let i=0; i<40; i++) arr.push(randomFakeRow())
  // Ordena por score e percent
  return arr.sort((a, b) => b.score - a.score || b.percent - a.percent)
})
// --- FIM SIMULAÇÃO FAKES ---

const liveEngagement = computed(() => {
  const participantCount = currentLive.value?.engagement?.participantCount || 0
  const answersCorrect = currentLive.value?.engagement?.answersCorrect || 0
  const answersIncorrect = currentLive.value?.engagement?.answersIncorrect || 0
  const answersUnanswered = currentLive.value?.engagement?.answersUnanswered || 0
  const total = answersCorrect + answersIncorrect + answersUnanswered
  const correctPercentual = Math.round((answersCorrect/total)*100)
  const incorrectPercentual = Math.round((answersIncorrect/total)*100)
  const unansweredPercentual = 100 - correctPercentual - incorrectPercentual
  return {
    participantCount,
    answersCorrect,
    answersIncorrect,
    answersUnanswered,
    correctPercentual,
    incorrectPercentual,
    unansweredPercentual
  }
})

let lobbyTimeout: ReturnType<typeof setTimeout> | null = null
let eventSource: EventSource | null = null

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
      }
  )
}

async function removePupil(pupil: string) {
  const teacher = currentLive.value?.teacher
  if (!teacher) return
  const liveKey = `LIVE${teacher.login}CODE${teacher.code}`
  // Separar login e code
  const [login, code] = pupil.split('#')
  if (!login || !code) return
  await liveApi.removePupilToLobby(login, code, liveKey)
}

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

function toggleMonitor() {
  monitorOpen.value = !monitorOpen.value
}

function closeMonitor() {
  monitorOpen.value = false
}

function loginColor(login: string) {
  // Gera cor suave baseada no hash do login
  const colors = ['#90caf9', '#a5d6a7', '#ffe082', '#f48fb1', '#ce93d8', '#b0bec5']
  let hash = 0
  for (let i = 0; i < login.length; i++) hash += login.charCodeAt(i)
  return colors[hash % colors.length]
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

          <div v-else class="centered-message">

            <q-card-section class="bg-grey-8 q-pa-md">
              <div class="text-subtitle2 q-mb-sm">Live Preview</div>
              <QuizPreview
                  v-if="previewType.startsWith('QUIZ_')"
                  :type="previewType"
                  :data="previewData"
              />
              <SlidePreview
                  v-else-if="previewType.startsWith('SLIDE_')"
                  :type="previewType"
                  :data="previewData"
              />
            </q-card-section>

          </div>

        </div>
      </div>
      <div v-if="isOwnerTeacher" class="live-controls-footer row items-center full-width no-wrap">
        <div class="row items-center control-group justify-end full-width">
          <q-btn
            v-if="currentPosition === 0"
            class="control-btn start-btn q-mr-md"
            flat
            label="Start"
            @click="startSession"
            color="grey-6"
            text-color="#2196f3" />
          <q-btn
            dense flat
            label="Monitor"
            class="control-btn monitor-btn q-mr-md"
            :color="monitorOpen ? 'green-6' : 'grey-6'"
            :text-color="monitorOpen ? '#fff' : '#43a047'"
            @click="toggleMonitor"
          />
          <q-btn label="Prev" @click="goToPreviousPosition" :disable="currentPosition <= 1" class="control-btn prev-btn q-mr-sm" color="grey-6" text-color="#ffe066" flat />
          <q-btn label="Next" @click="goToNextPosition" :disable="currentPosition === 0" class="control-btn next-btn q-mr-sm" color="grey-6" text-color="#ffd600" flat />
          <q-btn label="End" @click="confirmEnd" class="control-btn end-btn" color="grey-6" text-color="#ff5252" flat />
        </div>
      </div>

      <!-- Novo painel Monitor -->
      <transition name="slide-up">
        <div v-if="monitorOpen && isOwnerTeacher" class="monitor-panel bg-grey-10 text-white row items-center full-width no-wrap">
          <div class="row items-center full-width no-wrap justify-between q-pa-md monitor-header">
            <div class="row items-center full-width no-wrap engagement-header-row">
              <div class="row items-center engagement-title-row">
                <div class="text-h6 text-weight-bold">Monitor</div>
                <div class="engagement-box row items-center">
                  <span class="engagement-label">Engagement</span>
                  <span class="engagement-detail">{{ liveEngagement.participantCount }} participants</span>
                </div>

                <div class="engagement-box row items-center">
                  <span class="engagement-label">Correct</span>
                  <q-linear-progress :value="liveEngagement.correctPercentual/100" color="green-5" track-color="grey-8" rounded style="width:80px;height:12px;" />
                  <span class="engagement-label">{{
                      liveEngagement.answersCorrect
                    }} ({{ liveEngagement.correctPercentual }}%)</span>
                </div>
                <div class="engagement-box row items-center">
                  <span class="engagement-label">Incorrect</span>
                  <q-linear-progress :value="liveEngagement.incorrectPercentual/100" color="red-5" track-color="grey-8" rounded style="width:80px;height:12px;" />
                  <span class="engagement-label">{{
                      liveEngagement.answersIncorrect
                    }} ({{ liveEngagement.incorrectPercentual }}%)</span>
                </div>
                <div class="engagement-box row items-center">
                  <span class="engagement-label">Unanswered</span>
                  <q-linear-progress :value="liveEngagement.unansweredPercentual/100" color="grey-5" track-color="grey-8" rounded style="width:80px;height:12px;" />
                  <span class="engagement-label">{{
                      liveEngagement.answersUnanswered
                    }} ({{ liveEngagement.unansweredPercentual }}%)</span>
                </div>

              </div>
            </div>
            <q-btn flat dense icon="close" @click="closeMonitor" color="grey-4" />
          </div>
          <div class="monitor-table-wrap items-center full-width">
            <q-table
              :rows="monitorRows"
              :columns="[
                { name: 'idx', label: 'Ranking', align: 'left', field: row => row },
                { name: 'login', label: 'User', align: 'left', field: 'login' },
                { name: 'score', label: 'Score', align: 'left', field: 'score' },
                { name: 'total', label: 'Answers', align: 'left', field: 'total' },
                { name: 'percent', label: 'Hit %', align: 'left', field: 'percent' },
                { name: 'last', label: 'Last', align: 'left', field: 'lastHit' },
                { name: 'actions', label: '', align: 'left', field: 'raw' }
              ]"
              row-key="raw"
              flat
              dense
              hide-pagination
              :rows-per-page-options="[0]"
              :pagination="{rowsPerPage: 9999}"
              class="monitor-table custom-monitor-table"
            >
              <template #body-cell-idx="props">
                <q-td class="table-idx">
                  <span class="avatar-ranking">{{ props.pageIndex + 1 }}</span>
                </q-td>
              </template>
              <template #body-cell-login="props">
                <q-td class="q-gutter-x-sm">
                  <span class="avatar-login-square" :style="'background:' + loginColor(props.row.login)">
                    {{ props.row.login.slice(0, 3).toUpperCase() }}
                  </span>
                  <span class="login-label">{{ props.row.login }}</span>
                  <q-badge color="grey-3" text-color="#666" class="code-badge">#{{ props.row.code }}</q-badge>
                </q-td>
              </template>
              <template #body-cell-score="props">
                <q-td class="text-bold">
                  <q-badge :color="props.row.score > 0 ? 'green-6' : 'red-5'" text-color="white">{{ props.row.score }}</q-badge>
                </q-td>
              </template>
              <template #body-cell-total="props">
                <q-td class="table-total">
                  <span class="answers-bold">{{ props.row.total }}</span>
                </q-td>
              </template>
              <template #body-cell-percent="props">
                <q-td>
                  <q-badge :color="props.row.percent >= 80 ? 'green-6' : props.row.percent >= 50 ? 'yellow-8' : 'red-5'" text-color="white">{{ props.row.percent }}%</q-badge>
                </q-td>
              </template>
              <template #body-cell-last="props">
                <q-td>
                  <q-icon v-if="props.row.lastHit === true" name="check_circle" color="green-5" size="sm" />
                  <q-icon v-else-if="props.row.lastHit === false" name="cancel" color="red-5" size="sm" />
                </q-td>
              </template>
              <template #body-cell-actions="props">
                <q-td align="right">
                  <q-btn flat dense icon="person_remove" color="negative" @click="removePupil(props.row.raw)" />
                </q-td>
              </template>
            </q-table>
          </div>
        </div>
      </transition>
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
          :error="!!confirmEndInput && !canConfirmEnd"
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
  overflow: hidden;
}

.live-modal-content-row {
  display: flex;
  flex-direction: row;
  width: 100vw;
  height: calc(100vh - 80px);
  position: relative;
}

.main-content-empty {
  flex: 1 1 0;
  min-height: 60vh;
  position: relative;
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

.monitor-toggle-btn {
  min-width: 92px;
  font-weight: 600;
  border-radius: 8px;
  background: #232526;
  color: #43a047;
}

.monitor-panel {
  position: absolute;
  bottom: 80px;
  padding: 16px 32px;
  z-index: 200;
  background: #232526;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  box-shadow: 0 -2px 16px 0 rgba(0,0,0,0.18);
  transition: max-height 0.3s;
  max-height: 60vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.monitor-header {
  border-bottom: 1px solid #333;
  background: rgba(255,255,255,0.05);
  border-radius: 14px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08);
  padding: 6px 14px;
  gap: 6px;
  width: 100%;
  margin-bottom: 8px;
}

.monitor-table-wrap {
  flex: 1 1 auto;
  overflow-y: auto;
  max-height: 48vh;
  padding-bottom: 8px;
  background: rgba(255,255,255,0.05);
  border-radius: 14px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.08);
  padding: 6px 14px;
  gap: 6px;
  width: 100%;
  /* Scrollbar custom elegante */
  scrollbar-width: thin;
  scrollbar-color: #ffe066 #232526;
}
.monitor-table-wrap::-webkit-scrollbar {
  width: 7px;
  background: #232526;
  border-radius: 8px;
}
.monitor-table-wrap::-webkit-scrollbar-thumb {
  background: #ffe066;
  border-radius: 8px;
  min-height: 28px;
}
.monitor-table-wrap::-webkit-scrollbar-thumb:hover {
  background: #ffd600;
}

.monitor-table {
  background: transparent;
}

.slide-up-enter-active, .slide-up-leave-active {
  transition: all 0.3s cubic-bezier(.4,0,.2,1);
}

.slide-up-enter-from, .slide-up-leave-to {
  transform: translateY(100%);
  opacity: 0;
}

.slide-up-enter-to, .slide-up-leave-from {
  transform: translateY(0);
  opacity: 1;
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

.monitor-btn {
  font-weight: 700;
  color: #43a047 !important;
  border-radius: 8px !important;
  min-width: 92px;
  font-size: 0.98rem;
  box-shadow: none;
  background: none !important;
  transition: color 0.2s, background 0.2s;
}

.monitor-btn:active, .monitor-btn:focus, .monitor-btn[aria-pressed="true"] {
  color: #fff !important;
  background: #43a047 !important;
}

.end-dialog-card {
  min-width: 340px;
  border-radius: 14px;
  box-shadow: 0 2px 16px 0 rgba(0,0,0,0.18);
  background: #232526 !important;
  color: #fff !important;
}

.custom-monitor-table .q-table__middle,
.custom-monitor-table .q-table__bottom,
.custom-monitor-table .q-table__top {
  display: none !important;
}
.custom-monitor-table .q-tr {
  border-radius: 8px;
  background: rgba(255,255,255,0.02);
  margin-bottom: 2px;
}
.custom-monitor-table .q-td {
  font-size: 1.02rem;
  padding-top: 6px;
  padding-bottom: 6px;
  vertical-align: middle;
}
.custom-monitor-table .q-td[data-col="login"] {
  padding-left: 12px;
  text-align: left;
}
.custom-monitor-table .q-td[data-col="score"],
.custom-monitor-table .q-td[data-col="total"],
.custom-monitor-table .q-td[data-col="percent"],
.custom-monitor-table .q-td[data-col="last"] {
  text-align: center;
}
.custom-monitor-table th[data-col="login"] {
  padding-left: 12px;
  text-align: left;
}
.custom-monitor-table th[data-col="score"],
.custom-monitor-table th[data-col="total"],
.custom-monitor-table th[data-col="percent"],
.custom-monitor-table th[data-col="last"] {
  text-align: center;
}

.custom-monitor-table th,
.custom-monitor-table .table-idx {
  font-weight: 800 !important;
}
.custom-monitor-table th[data-col="idx"],
.custom-monitor-table .q-td[data-col="idx"] {
  text-align: center;
  font-weight: 800 !important;
  padding-left: 0 !important;
  padding-right: 0 !important;
  width: 36px;
}
.custom-monitor-table th[data-col="login"],
.custom-monitor-table .q-td[data-col="login"] {
  text-align: left;
  padding-left: 12px !important;
}
.custom-monitor-table th[data-col="score"],
.custom-monitor-table th[data-col="total"],
.custom-monitor-table th[data-col="percent"],
.custom-monitor-table th[data-col="last"],
.custom-monitor-table .q-td[data-col="score"],
.custom-monitor-table .q-td[data-col="total"],
.custom-monitor-table .q-td[data-col="percent"],
.custom-monitor-table .q-td[data-col="last"] {
  text-align: center;
  padding-left: 0 !important;
  padding-right: 0 !important;
}
.custom-monitor-table th[data-col="actions"],
.custom-monitor-table .q-td[data-col="actions"] {
  text-align: right;
  padding-right: 8px !important;
}

.avatar-login-square {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 46px;
  height: 32px;
  padding: 0 8px;
  border-radius: 7px;
  font-weight: 700;
  font-size: 0.97rem;
  color: #fff;
  margin-right: 8px;
  box-shadow: 0 1px 4px 0 rgba(0,0,0,0.09);
  letter-spacing: 1px;
  border: 2px solid #fffbe7;
  background-clip: padding-box;
  transition: background 0.2s, border 0.2s;
  text-align: center;
}

.login-label {
  font-weight: 600;
  font-size: 1.05rem;
  color: #e3f2fd;
  margin-right: 8px;
}
.code-badge {
  font-size: 0.93rem;
  font-weight: 500;
  border-radius: 6px;
  padding: 2px 8px;
  background: #ececec !important;
  color: #666 !important;
}

.avatar-ranking {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  height: 32px;
  padding: 0 6px;
  border-radius: 8px;
  font-weight: 700;
  font-size: 1.01rem;
  color: #fff;
  background: #90caf9;
  box-shadow: 0 1px 4px 0 rgba(0,0,0,0.07);
  letter-spacing: 1px;
  border: 2px solid #fffbe7;
  background-clip: padding-box;
}

.answers-bold {
  font-weight: 700;
  color: #64b5f6;
  font-size: 1.04rem;
  letter-spacing: 1px;
}

.engagement-box.row.items-center {
  background: rgba(255,224,102,0.08);
  border-radius: 10px;
  padding: 6px 16px 6px 12px;
  align-items: center;
  min-height: 38px;
  font-size: 1.09rem;
  font-weight: 600;
  margin-right: 12px;
  gap: 10px;
}
.engagement-label {
  color: #ffe066;
  font-weight: 800;
  margin-right: 6px;
  font-size: 1.09rem;
}
.engagement-detail {
  color: #e0e0e0;
  font-size: 1.09rem;
  font-weight: 600;
  margin-left: 6px;
}

.engagement-header-row {
  align-items: center;
  flex-wrap: nowrap;
  width: 100%;
}

.engagement-title-row {
  align-items: center;
  gap: 18px;
  flex-shrink: 0;
  margin: 0 !important;
  top: auto !important;
  left: auto !important;
}
</style>