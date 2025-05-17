import { createApp } from 'vue'
import { Quasar, Notify } from 'quasar' // Importe Notify explicitamente

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

    // Configuração completa do Quasar
    myApp.use(Quasar, {
        plugins: {
            Notify // Adicione Notify aos plugins
        },
        config: {
            notify: { // Configurações padrão do Notify
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

    // Inicialização do app
    myApp.use(router).mount('#app')
})()