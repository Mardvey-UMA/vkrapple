import { ReviewDTO } from './review'

export interface ProductResponse {
	id: number
	article_number: number
	name: string
	price: number
	rating: number
	number_of_orders: number
	attributes: ProductAttributeDTO[]
	category_id: number
	category_name: string
	photos: string[]
	reviews: ReviewDTO[]
	inCart?: number
	inWishlist?: boolean
}

export interface ProductAttributeDTO {
	attribute_id: number
	attribute_name: string
	value: string
}

export interface ProductPageResponse {
	products: ProductResponse[]
	current_page: number
	total_pages: number
	total_products: number
}
