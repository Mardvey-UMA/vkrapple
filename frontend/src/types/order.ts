export interface CreateOrderRequest {
	paymentMethod: 'CASH' | 'CARD' | 'CRYPTO'
	orderAddress: string
}

export interface OrderDTO {
	id: number
	status: 'CREATED' | 'PROCESSING' | 'DELIVERED' | 'CANCELLED'
	orderAmount: number
	paymentMethod: string
	orderAddress: string
	expectedDate: string
	createdAt: string
	items: OrderItemDTO[]
}

export interface OrderItemDTO {
	articleNumber: number
	productName: string
	quantity: number
	price: number
}

export interface OrderPageResponse {
	orders: OrderDTO[]
	currentPage: number
	totalPages: number
	totalOrders: number
}
