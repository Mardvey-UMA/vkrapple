import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

const queryClient = new QueryClient({
	defaultOptions: {
		queries: {
			staleTime: 5 * 60 * 1000,
			gcTime: 15 * 60 * 1000,
			retry: 2,
			refetchOnWindowFocus: false,
			retryDelay: attempt => Math.min(attempt * 1000, 30 * 1000),
		},
	},
})

export const QueryProvider = ({ children }: { children: React.ReactNode }) => (
	<QueryClientProvider client={queryClient}>
		{children}
		<ReactQueryDevtools position='bottom' />
	</QueryClientProvider>
)
