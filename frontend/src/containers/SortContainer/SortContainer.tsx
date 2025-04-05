import { useState } from 'react'
import {
	SortControl,
	SortOption,
} from '../../components/SortControl/SortControl'

export const SortContainer = () => {
	const [currentSort, setCurrentSort] = useState<SortOption>('price_asc')

	return <SortControl currentSort={currentSort} onSortChange={setCurrentSort} />
}
