<script setup lang="ts">
import { ref } from 'vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const dialog = ref(false)
const quizKey = ref('')

function open(key: string) {
  quizKey.value = key
  dialog.value = true
}

function close() {
  dialog.value = false
}

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
    <q-card class="bg-grey-10 text-white">
      <q-bar>
        <div>Live Quiz: {{ quizKey }}</div>
        <q-space />
        <q-btn dense flat icon="close" @click="close">
          <q-tooltip class="bg-white text-primary">Close</q-tooltip>
        </q-btn>
      </q-bar>

      <q-card-section class="text-center q-mt-xl">
        <div class="text-h4">Quiz Key: {{ quizKey }}</div>
        <div class="text-subtitle1 q-mt-md">This is where your live quiz would be displayed</div>

        <q-btn
            color="yellow-14"
            label="Start Live Session"
            class="q-mt-xl"
            size="lg"
            @click="$q.notify({ message: 'Live session started!', color: 'positive' })"
        />
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<style scoped>
.q-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>