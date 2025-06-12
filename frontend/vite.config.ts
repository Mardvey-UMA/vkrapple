// //import tailwindcss from '@tailwindcss/vite'
// import react from '@vitejs/plugin-react-swc'
// import { defineConfig } from 'vite'

// export default defineConfig({
// 	plugins: [react()],
// 	server: {
// 		host: true,
// 		allowedHosts: ['www.ssushop.ru', 'ssushop.ru']
// 	},
// 	preview: {
// 	    host: true,
// 	    port: 3000,
// 	    allowedHosts: [
// 	      "www.ssushop.ru",
// 	      "ssushop.ru",
// 	      "localhost"
// 	    ]
// 	  }
// })
import react from '@vitejs/plugin-react-swc'
import { defineConfig } from 'vite'

// vite.config.js  (клиент)
export default defineConfig({
	plugins: [react()],
	server: {
		host: '0.0.0.0',
		port: 5173,
		strictPort: true,
		allowedHosts: ['webshopvkr2.duckdns.org'],
		proxy: {
			'/api': {
				target: 'https://api-webshopvkr2.duckdns.org',
				changeOrigin: true,
				secure: true,
			},
		},
	},
})
