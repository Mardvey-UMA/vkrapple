export interface CategoryResponse {
	categories: Record<string, number>
}

export interface CategoryAttributesResponse {
	categoryId: number
	categoryName: string
	attributes: AttributeDTO[]
}

export interface AttributeDTO {
	id: number
	name: string
	values: string[]
}
