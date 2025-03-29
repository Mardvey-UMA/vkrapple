import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

interface AuthResponse {
	access_token: string
	access_expires_at: string
	issued_at: string
	refresh_token: string
	refresh_expires_at: string
}

interface UserData {
	id: number
}

export default function App() {
	initializeTelegramApp()
	const [requestsStatus, setRequestsStatus] = useState<{
		auth: 'pending' | 'success' | 'error'
		me: 'pending' | 'success' | 'error'
	}>({ auth: 'pending', me: 'pending' })

	const { data: authData, error: authError } = useQuery<AuthResponse>({
		queryKey: ['authenticate'],
		queryFn: async () => {
			try {
				const response = await api.post<AuthResponse>('/auth/authenticate')
				localStorage.setItem('accessToken', response.data.access_token)
				localStorage.setItem('refreshToken', response.data.refresh_token)
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
		retry: 0,
		enabled: !!authData,
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
