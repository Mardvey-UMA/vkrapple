import { ReviewDTO } from './review'

export interface ProductResponse {
	id: number
	articleNumber: number // Совпадает с DTO
	name: string
	price: number
	rating: number
	attributes: ProductAttributeDTO[]
	categoryId: number
	categoryName: string
	photos: string[]
	reviews: ReviewDTO[]
}

export interface ProductAttributeDTO {
	attributeId: number
	attributeName: string
	value: string
}

export interface ProductPageResponse {
	products: ProductResponse[]
	currentPage: number
	totalPages: number
	totalProducts: number
}
