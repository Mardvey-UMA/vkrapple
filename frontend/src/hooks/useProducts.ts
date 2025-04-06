import { keepPreviousData, useQuery } from '@tanstack/react-query'
import { useSearchParams } from 'react-router-dom'
import { ProductService } from '../api/product'
import type { ProductPageResponse, ProductResponse } from '../types/product'
import { useProductStatus } from './useProductStatus'

export const useProducts = () => {
	const [searchParams] = useSearchParams()
	const page = Number(searchParams.get('page')) || 0
	const categoryId = Number(searchParams.get('categoryId')) || undefined
	const sort = searchParams.get('sort') || undefined
	const searchQuery = searchParams.get('search') || undefined

	const { cartMap, wishlistSet, isLoading: statusLoading } = useProductStatus()

	const productsQuery = useQuery<ProductPageResponse>({
		queryKey: [
			'products',
			{
				page,
				categoryId,
				sort,
				search: searchQuery,
				...Object.fromEntries(searchParams),
			},
		],
		queryFn: () => {
			const filters = Array.from(searchParams.entries()).reduce(
				(acc, [key, value]) => {
					if (key.startsWith('attributes[')) {
						const attrId = key.match(/\[(.*?)\]/)?.[1]
						if (attrId) {
							acc[Number(attrId)] = [...(acc[Number(attrId)] || []), value]
						}
					}
					return acc
				},
				{} as Record<number, string[]>
			)

			if (searchQuery) {
				return ProductService.search(searchQuery, page)
			}

			if (categoryId) {
				return ProductService.searchWithFilters(
					categoryId,
					filters,
					page,
					20,
					sort
				)
			}

			return ProductService.getAll(page, 20, sort)
		},
		placeholderData: keepPreviousData,
		staleTime: 1000 * 60 * 5,
		enabled: !statusLoading,
	})

	const enrichedProducts =
		productsQuery.data?.products?.map(product => ({
			...product,
			inCart: cartMap.get(product.article_number) || 0,
			inWishlist: wishlistSet.has(product.article_number),
		})) || []

	return {
		...productsQuery,
		data: productsQuery.data
			? {
					...productsQuery.data,
					products: enrichedProducts,
			  }
			: undefined,
	}
}
export const useProductDetails = (article: number) => {
	return useQuery<ProductResponse, Error>({
		queryKey: ['product', article],
		queryFn: () => ProductService.getProduct(article),
		enabled: !!article,
	})
}
