import { createApp } from 'vue'
import { Quasar, Notify, BottomSheet, Loading, Dialog } from 'quasar'
import { createPinia } from 'pinia'

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
    const pinia = createPinia()

    // Configuração completa do Quasar
    myApp.use(Quasar, {
        plugins: {
            Notify,
            BottomSheet,
            Loading,
            Dialog
        },
        config: {
            notify: {
                position: 'top',
                timeout: 2500,
                textColor: 'white'
            },
            loading: {
                spinnerColor: 'yellow',
                spinnerSize: 60,
                backgroundColor: 'grey-10',
                messageColor: 'white'
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
        .use(pinia)
        .use(router)
        .mount('#app')
})()