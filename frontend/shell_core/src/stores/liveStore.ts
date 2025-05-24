import { ref } from 'vue'
import type { ILive } from '../services/live/types/ILive';
import type { ITeacher } from '../services/live/types/ITeacher';

const LIVE_STORE_KEY = 'currentLive';

const liveRef = ref<ILive | null>(null)

function syncToLocalStorage(live: ILive | null) {
  if (live) {
    localStorage.setItem(LIVE_STORE_KEY, JSON.stringify(live))
  } else {
    localStorage.removeItem(LIVE_STORE_KEY)
  }
}

export const liveStore = {
  createLive(live: ILive) {
    liveRef.value = live
    syncToLocalStorage(live)
  },

  getLive(): ILive | null {
    return liveRef.value
  },

  updateLive(partial: Partial<ILive>) {
    if (!liveRef.value) return
    liveRef.value = { ...liveRef.value, ...partial }
    syncToLocalStorage(liveRef.value)
  },

  removeLive() {
    liveRef.value = null
    syncToLocalStorage(null)
  },

  setTeacher(teacher: ITeacher) {
    if (!liveRef.value) return
    liveRef.value = { ...liveRef.value, teacher }
    syncToLocalStorage(liveRef.value)
  },
}

// Carrega do localStorage ao iniciar
const stored = localStorage.getItem(LIVE_STORE_KEY)
if (stored) {
  try {
    liveRef.value = JSON.parse(stored)
  } catch {}
}
