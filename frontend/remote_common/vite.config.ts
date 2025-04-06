import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'url'
import { quasar, transformAssetUrls } from '@quasar/vite-plugin'
import vue from '@vitejs/plugin-vue'
import federation from "@originjs/vite-plugin-federation";


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(
        {
          template: { transformAssetUrls }
        }
    ),

    // @quasar/plugin-vite options list:
    // https://github.com/quasarframework/quasar/blob/dev/vite-plugin/index.d.ts
    quasar({
      sassVariables: fileURLToPath(
          new URL('./src/styles/quasar-variables.sass', import.meta.url)
      )
    }),

    federation({
      name: 'remote-common',
      filename: 'remoteEntry.js',
      exposes: {
        './SidebarB': './src/components/SidebarB.vue',
      },
      shared: ['vue', 'quasar']
    })
  ],

  build: {
    outDir: 'dist',
    modulePreload: false,
    target: 'esnext',
    minify: false,
    cssCodeSplit: false,
    sourcemap: true
  },

  server: {
    port: 3001,
  }

})
