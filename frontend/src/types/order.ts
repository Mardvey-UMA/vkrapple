export interface CreateOrderRequest {
	payment_method: 'CASH' | 'CARD' | 'CRYPTO'
	order_address: string
}

export interface OrderDTO {
	id: number
	status: 'CREATED' | 'PROCESSING' | 'DELIVERED' | 'CANCELLED'
	order_amount: number
	payment_method: string
	order_address: string
	expected_date: string
	created_at: string
	items: OrderItemDTO[]
}

export interface OrderItemDTO {
	article_number: number
	product_name: string
	quantity: number
	price: number
}

export interface OrderPageResponse {
	orders: OrderDTO[]
	current_page: number
	total_pages: number
	total_orders: number
}
