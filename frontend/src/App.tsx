import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

interface AuthResponse {
	
	accessToken: string
	refreshToken: string
}

interface UserData {
	// Добавьте правильные типы для вашего ответа /me
	id: string
	username: string
}

export default function App() {
	initializeTelegramApp()
	const [requestsStatus, setRequestsStatus] = useState<{
		auth: 'pending' | 'success' | 'error'
		me: 'pending' | 'success' | 'error'
	}>({ auth: 'pending', me: 'pending' })

	// Запрос для аутентификации
	const { data: authData, error: authError } = useQuery<AuthResponse>({
		queryKey: ['authenticate'],
		queryFn: async () => {
			try {
				const response = await api.post<AuthResponse>('/auth/authenticate')
				localStorage.setItem('accessToken', response.data.accessToken)
				localStorage.setItem('refreshToken', response.data.refreshToken)
				setRequestsStatus(prev => ({ ...prev, auth: 'success' }))
				return response.data
			} catch (error) {
				setRequestsStatus(prev => ({ ...prev, auth: 'error' }))
				throw error
			}
		},
		retry: 0,
		staleTime: Infinity,
	})

	// Запрос для получения данных пользователя
	const { data: userData, error: userError } = useQuery<UserData>({
		queryKey: ['me'],
		queryFn: async () => {
			try {
				const response = await api.get<UserData>('/me')
				setRequestsStatus(prev => ({ ...prev, me: 'success' }))
				return response.data
			} catch (error) {
				setRequestsStatus(prev => ({ ...prev, me: 'error' }))
				throw error
			}
		},
		retry: 3,
		enabled: !!authData, // Запускаем только после успешной аутентификации
		staleTime: Infinity,
	})

	const getStatusMessage = () => {
		if (requestsStatus.auth === 'pending') return 'Authenticating...'
		if (requestsStatus.auth === 'error')
			return `Authentication Error: ${authError?.message}`
		if (requestsStatus.me === 'pending') return 'Fetching user data...'
		if (requestsStatus.me === 'error')
			return `User Data Error: ${userError?.message}`
		return 'All requests completed successfully!'
	}

	return (
		<div>
			<h2>Request Status:</h2>
			<div>{getStatusMessage()}</div>

			<h2>Authentication Data:</h2>
			<pre>{JSON.stringify(authData, null, 2)}</pre>

			<h2>User Data:</h2>
			<pre>{JSON.stringify(userData, null, 2)}</pre>
		</div>
	)
}
