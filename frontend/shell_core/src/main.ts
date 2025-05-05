import { createApp } from 'vue'
import { Quasar } from 'quasar'

// Import icon libraries
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar css
import 'quasar/src/css/index.sass'

// Assumes your root component is App.vue
// and placed in same folder as main.js
import App from './App.vue'
import { AuthApi } from "./services/auth/AuthApi.ts";

(async () => {
    const authApi = new AuthApi()
    await authApi.initAnonymousToken();

    const myApp = createApp(App)

    myApp.use(Quasar, {
        plugins: {},
    })

    myApp.config.errorHandler = (err, instance, info) => {
        console.error(`Erro capturado: ${err}`)
        console.log(`Componente:`, instance)
        console.log(`Informação:`, info)
    }

    myApp.config.warnHandler = (msg, instance, trace) => {
        console.warn(`Alerta capturado: ${msg}`)
        console.log(`Componente:`, instance)
        console.log(`Trace:`, trace)
    }

    myApp.mount('#app')
})()