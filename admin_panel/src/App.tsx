import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './auth/AuthProvider'
import { PrivateRoute } from './auth/PrivateRoute'
import AnalyticsPage from './pages/AnalyticsPage'
import CategoriesPage from './pages/CategoriesPage'
import CreateProductPage from './pages/CreateProductPage'
import DashboardPage from './pages/DashboardPage'
import DeleteProductPage from './pages/DeleteProductPage'
import LoginPage from './pages/LoginPage'

const queryClient = new QueryClient()

export default function App() {
	return (
		<QueryClientProvider client={queryClient}>
			<BrowserRouter>
				<AuthProvider>
					<Routes>
						<Route path='/login' element={<LoginPage />} />
						<Route element={<PrivateRoute />}>
							<Route path='/*' element={<DashboardPage />} />
							<Route path='categories' element={<CategoriesPage />} />
							<Route path='products/new' element={<CreateProductPage />} />
							<Route path='products/delete' element={<DeleteProductPage />} />
							<Route path='analytics' element={<AnalyticsPage />} />
						</Route>
					</Routes>
				</AuthProvider>
			</BrowserRouter>
		</QueryClientProvider>
	)
}
