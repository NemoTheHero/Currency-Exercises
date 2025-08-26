import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // server: {
  //   proxy: {
  //     '/api': { // This is the prefix for API requests in your frontend
  //       target: 'http://localhost:8080', // The URL of your backend server
  //       changeOrigin: true, // Ensures the request appears to originate from the target domain
  //       secure: false, // Set to true for production if using HTTPS with a valid certificate
  //       rewrite: (path) => path.replace(/^\/api/, ''), // Optional: rewrites the path if needed
  //     },
  //   },
  // },
})
