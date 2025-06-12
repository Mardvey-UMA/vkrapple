import react from '@vitejs/plugin-react-swc'
import { defineConfig } from 'vite'

// vite.config.js  (админка)
export default defineConfig({
	plugins: [react()],
	server: {
		host: '0.0.0.0',
		port: 5173,
		strictPort: true,
		allowedHosts: ['admin-webshopvkr2.duckdns.org'],
		proxy: {
			'/api': {
				target: 'https://api-webshopvkr2.duckdns.org',
				changeOrigin: true,
				secure: true,
			},
		},
	},
})
