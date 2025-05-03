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

export default defineConfig({
	plugins: [react()],
	server: {
		host: '0.0.0.0',
		port: 5173,
		strictPort: true,
		allowedHosts: ['frontend.localhost'],

		proxy: {
			'/api': {
				target: 'https://backend.localhost',
				changeOrigin: true,
				secure: false,
			},
		},
	},
})
