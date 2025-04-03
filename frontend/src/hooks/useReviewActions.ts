import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ReviewService } from '../api'
import { ReviewDTO } from '../types/review'

export const useReviewActions = () => {
	const queryClient = useQueryClient()

	const createReview = useMutation({
		mutationFn: (data: { article: number; review: ReviewDTO }) =>
			ReviewService.create(data.article, data.review),
		onSuccess: (_, vars) => {
			queryClient.invalidateQueries({
				queryKey: ['reviews', vars.article],
			})
			queryClient.invalidateQueries({
				queryKey: ['product', vars.article],
			})
		},
	})

	const uploadPhoto = useMutation({
		mutationFn: (data: { reviewId: number; index: number; file: File }) =>
			ReviewService.uploadPhoto(
				{ reviewId: data.reviewId, indexNumber: data.index },
				data.file
			),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['reviews'] })
		},
	})

	return { createReview, uploadPhoto }
}
