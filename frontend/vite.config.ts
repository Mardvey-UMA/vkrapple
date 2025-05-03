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
		host: true,
		proxy: {
			'/api': {
				target: 'https://backend.localhost',
				changeOrigin: true,
			},
		},
	},
	preview: {
		host: true,
		port: 5173,
		allowedHosts: [
			'www.ssushop.ru',
			'ssushop.ru',
			'localhost',
			'backend.localhost',
		],
	},
})
