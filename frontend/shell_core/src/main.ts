import { createApp } from 'vue'
import { Quasar, Notify, BottomSheet } from 'quasar'
import { createPinia } from 'pinia' // Importe o Pinia

// Import icon libraries
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar css
import 'quasar/src/css/index.sass'

// Assumes your root component is App.vue
import App from './App.vue'
import { AuthApi } from "./services/auth/AuthApi.ts"
import router from "./routes.ts"

(async () => {
    const authApi = new AuthApi()
    await authApi.initAnonymousToken()

    const myApp = createApp(App)
    const pinia = createPinia() // Crie a instância do Pinia

    // Configuração completa do Quasar
    myApp.use(Quasar, {
        plugins: {
            Notify,
            BottomSheet
        },
        config: {
            notify: {
                position: 'top',
                timeout: 2500,
                textColor: 'white'
            }
        }
    })

    // Handlers de erro (opcional)
    myApp.config.errorHandler = (err) => {
        console.error('Erro capturado:', err)
        Notify.create({
            type: 'negative',
            message: 'Ocorreu um erro inesperado',
            caption: String(err)
        })
    }

    myApp.config.warnHandler = (msg) => {
        console.warn('Alerta capturado:', msg)
    }

    myApp
        .use(pinia) // Adicione o Pinia antes do router
        .use(router)
        .mount('#app')
})()