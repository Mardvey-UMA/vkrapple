import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react-swc'
import { defineConfig } from 'vite'

export default defineConfig({
	plugins: [react(), tailwindcss()],
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
