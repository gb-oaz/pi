<template>
  <div class="live-api-test">
    <h2>Live API Tester</h2>

    <div class="controls">
      <q-input v-model="quizKey" label="Quiz Key" />
      <q-input v-model="liveKey" label="Live Key" />
      <q-input v-model="pupilLogin" label="Pupil Login" />
      <q-input v-model="pupilCode" label="Pupil Code" />
      <q-input v-model="teacherLogin" label="Teacher Login" />
      <q-input v-model="teacherPassword" label="Teacher Password" type="password" />
      <q-input v-model="pupilPassword" label="Pupil Password" type="password" />

      <div class="answer-items">
        <q-input v-model="answerItem" label="Answer Item" @keyup.enter="addAnswerItem" />
        <q-btn label="Add Answer" @click="addAnswerItem" />
        <div v-for="(item, index) in answerItems" :key="index" class="answer-item">
          {{ item }} <q-btn icon="delete" size="sm" flat @click="removeAnswerItem(index)" />
        </div>
      </div>
    </div>

    <div class="actions">
      <q-btn label="Init Anonymous Token" @click="initAnonymousToken" />
      <q-btn label="Teacher Sign In" @click="teacherSignIn" />
      <q-btn label="Pupil Sign In" @click="pupilSignIn" />
      <q-btn label="Sign Out" @click="signOut" />
      <q-btn label="Create Live" @click="createLive" />
      <q-btn label="Add Pupil to Lobby" @click="addPupilToLobby" />
      <q-btn label="Remove Pupil from Lobby" @click="removePupilFromLobby" />
      <q-btn label="Next Position" @click="nextPosition" />
      <q-btn label="Previous Position" @click="previousPosition" />
      <q-btn label="Add Answer" @click="addAnswer" />
      <q-btn label="End Live" @click="endLive" />
      <q-btn label="Get Live" @click="getLive" />
      <q-btn
          :label="streamActive ? 'Stop Stream' : 'Start Stream'"
          @click="toggleStream"
          :color="streamActive ? 'negative' : 'positive'"
      />
    </div>

    <div class="output">
      <h3>Output:</h3>
      <pre>{{ output }}</pre>
    </div>

    <div class="stream-data" v-if="streamData">
      <h3>Stream Data:</h3>
      <pre>{{ streamData }}</pre>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { AuthApi } from '../services/auth/AuthApi';
import { LiveApi } from '../services/live/LiveApi';
import type { ILive } from '../services/live/types/ILive';

export default defineComponent({
  name: 'LiveApiTester',
  data() {
    return {
      authApi: new AuthApi(),
      liveApi: new LiveApi(),
      quizKey: '19683cec63cf01f5fe4',
      liveKey: 'LIVEGUSTAVOBOAZCODE123456',
      pupilLogin: 'PAMELABOAZ',
      pupilCode: '123456',
      teacherLogin: 'GUSTAVOBOAZ',
      teacherCode: '123456',
      teacherPassword: 'Password@123456',
      pupilPassword: 'Password@123456',
      answerItem: '',
      answerItems: [] as string[],
      output: '',
      streamData: null as ILive | null,
      streamActive: false,
      eventSource: null as EventSource | null
    };
  },
  methods: {
    async logAction(action: string, promise: Promise<any>) {
      this.output += `> ${action}\n`;
      try {
        const result = await promise;
        this.output += `< ${JSON.stringify(result, null, 2)}\n\n`;
        return result;
      } catch (error) {
        this.output += `! ${error}\n\n`;
        throw error;
      }
    },

    async initAnonymousToken() {
      await this.logAction('initAnonymousToken', this.authApi.initAnonymousToken());
    },

    async teacherSignIn() {
      await this.logAction('teacherSignIn',
          this.authApi.signIn(this.teacherLogin, this.teacherCode, this.teacherPassword));
    },

    async pupilSignIn() {
      await this.logAction('pupilSignIn',
          this.authApi.signIn(this.pupilLogin, this.pupilCode, this.pupilPassword));
    },

    async signOut() {
      await this.logAction('signOut', this.authApi.signOut());
    },

    async createLive() {
      await this.logAction('createLive', this.liveApi.createLive(this.quizKey));
    },

    async addPupilToLobby() {
      await this.logAction('addPupilToLobby',
          this.liveApi.addPupilToLobby(this.liveKey));
    },

    async removePupilFromLobby() {
      await this.logAction('removePupilFromLobby',
          this.liveApi.removePupilToLobby(this.pupilLogin, this.pupilCode, this.liveKey));
    },

    async nextPosition() {
      await this.logAction('nextPosition',
          this.liveApi.nextPosition(this.liveKey));
    },

    async previousPosition() {
      await this.logAction('previousPosition',
          this.liveApi.previousPosition(this.liveKey));
    },

    async addAnswer() {
      await this.logAction('addAnswer',
          this.liveApi.addAnswerPupil(this.liveKey, this.answerItems));
    },

    async endLive() {
      await this.logAction('endLive',
          this.liveApi.endLive(this.liveKey));
    },

    async getLive() {
      await this.logAction('getLive',
          this.liveApi.getLive(this.liveKey));
    },

    async toggleStream() {
      if (this.streamActive) {
        this.stopStream();
      } else {
        await this.startStream();
      }
    },

    async startStream() {
      try {
        this.eventSource = await this.liveApi.getLiveStream(
            this.liveKey,
            (data) => {
              this.streamData = data;
              this.output += `<< Stream update: ${JSON.stringify(data, null, 2)}\n`;
            },
            (error) => {
              this.output += `!! Stream error: ${error}\n`;
              this.streamActive = false;
            }
        );
        this.streamActive = true;
        this.output += `> Started stream for live key: ${this.liveKey}\n`;
      } catch (error) {
        this.output += `! Failed to start stream: ${error}\n`;
      }
    },

    stopStream() {
      if (this.eventSource) {
        this.eventSource.close();
        this.eventSource = null;
      }
      this.streamActive = false;
      this.output += `> Stopped stream\n`;
    },

    addAnswerItem() {
      if (this.answerItem.trim()) {
        this.answerItems.push(this.answerItem.trim());
        this.answerItem = '';
      }
    },

    removeAnswerItem(index: number) {
      this.answerItems.splice(index, 1);
    }
  },
  beforeUnmount() {
    this.stopStream();
  }
});
</script>

<style scoped>
.live-api-test {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.controls {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.answer-items {
  grid-column: span 2;
}

.answer-item {
  display: inline-flex;
  align-items: center;
  margin-right: 8px;
  margin-bottom: 8px;
  padding: 4px 8px;
  background: #eee;
  border-radius: 4px;
}

.output, .stream-data {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  margin-top: 20px;
  max-height: 300px;
  overflow-y: auto;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>