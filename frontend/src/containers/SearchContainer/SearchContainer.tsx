import { Modal, Spin } from 'antd'
import { useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { BurgerMenu } from '../../components/BurgerMenu/BurgerMenu'
import { FilterForm } from '../../components/FilterForm/FilterForm'
import { SearchBar } from '../../components/SearchBar/SearchBar'
import { useCategoryFilters } from '../../hooks/useCategoryFilters'
import styles from './SearchContainer.module.scss'

export const SearchContainer = () => {
	const [isModalOpen, setIsModalOpen] = useState(false)
	const [searchParams, setSearchParams] = useSearchParams()
	const {
		categories,
		attributes,
		selectedCategory,
		selectedFilters,
		isLoading,
		handleCategoryChange,
		handleFilterChange,
		resetFilters,
	} = useCategoryFilters()

	const handleSearch = (value: string) => {
		console.log('Search value:', value)
	}

	const handleApplyFilters = () => {
		if (selectedCategory) {
			const newParams = new URLSearchParams()
			newParams.set('categoryId', selectedCategory.toString())

			Object.entries(selectedFilters).forEach(([attrId, values]) => {
				values.forEach(value =>
					newParams.append(`attributes[${attrId}]`, value)
				)
			})

			setSearchParams(newParams)
		}
		setIsModalOpen(false)
	}

	return (
		<div className={styles.container}>
			<SearchBar onSearch={handleSearch} />
			<BurgerMenu onClick={() => setIsModalOpen(true)} />

			<Modal
				title='Фильтры'
				open={isModalOpen}
				onCancel={() => setIsModalOpen(false)}
				footer={null}
				centered
				width='90%'
				style={{
					top: '5vh',
					maxWidth: '1440px',
				}}
				bodyStyle={{
					height: '85vh',
					overflowY: 'auto',
					padding: '16px 24px',
				}}
			>
				<div className={styles.modalContent}>
					{isLoading ? (
						<Spin />
					) : (
						<FilterForm
							categories={categories}
							attributes={attributes}
							selectedCategory={selectedCategory}
							selectedFilters={selectedFilters}
							onCategoryChange={handleCategoryChange}
							onFilterChange={handleFilterChange}
							onApply={handleApplyFilters}
							onReset={resetFilters}
						/>
					)}
				</div>
			</Modal>
		</div>
	)
}
