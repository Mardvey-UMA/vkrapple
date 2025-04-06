import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { CategoryService } from '../api/category'
import { CategoryAttributesResponse, CategoryResponse } from '../types/category'

export const useCategoryFilters = () => {
	const [selectedCategory, setSelectedCategory] = useState<number | null>(null)
	const [selectedFilters, setSelectedFilters] = useState<
		Record<number, string[]>
	>({})

	const { data: categories } = useQuery<CategoryResponse>({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})

	const { data: attributes, isLoading } = useQuery<CategoryAttributesResponse>({
		queryKey: ['categoryAttributes', selectedCategory],
		queryFn: () => CategoryService.getAttributes(selectedCategory!),
		enabled: !!selectedCategory,
	})

	const handleCategoryChange = (categoryId: number) => {
		setSelectedCategory(categoryId)
		setSelectedFilters({})
	}

	const handleFilterChange = (attributeId: number, values: string[]) => {
		setSelectedFilters(prev => ({
			...prev,
			[attributeId]: values,
		}))
	}

	const resetFilters = () => {
		setSelectedCategory(null)
		setSelectedFilters({})
	}

	return {
		categories: categories?.categories
			? Object.entries(categories.categories)
			: [],
		attributes: attributes?.attributes || [],
		selectedCategory,
		selectedFilters,
		isLoading,
		handleCategoryChange,
		handleFilterChange,
		resetFilters,
	}
}
