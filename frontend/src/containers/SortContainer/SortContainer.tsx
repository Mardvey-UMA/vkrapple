import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import {
	SortControl,
	SortOption,
} from '../../components/SortControl/SortControl'

export const SortContainer = () => {
	const [searchParams, setSearchParams] = useSearchParams()
	const [currentSort, setCurrentSort] = useState<SortOption>(
		(searchParams.get('sort') as SortOption) || 'price_asc'
	)

	const handleSortChange = (value: SortOption) => {
		setCurrentSort(value)
		searchParams.set('sort', value)
		setSearchParams(searchParams)
	}

	return (
		<SortControl currentSort={currentSort} onSortChange={handleSortChange} />
	)
}
