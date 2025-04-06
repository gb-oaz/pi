<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'SidebarB',
  props: {
    msg: { type: String, required: true }
  },
  setup() {
    const titulo = ref('Componente SidebarB');
    const contador = ref(0);

    const incrementar = () => { contador.value++; };

    return { titulo, contador, incrementar };
  }
});
</script>

<template>
  <main>
    <h1 v-text="msg" />
    <h2 v-text="titulo" />
    <button type="button" @click="incrementar">count is {{ contador }}</button>
  </main>
</template>

<style lang="css">
h1 { color: blue; }
h2 { color: green; }
</style>