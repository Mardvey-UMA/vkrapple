export interface ReviewDTO {
	id?: number
	rating: number
	text: string
	createdAt?: string
	photos?: string[]
}

export interface ReviewResponse {
	id: number
	rating: number
	text: string
}

export interface PhotoReviewRequest {
	reviewId: number
	indexNumber: number
}
