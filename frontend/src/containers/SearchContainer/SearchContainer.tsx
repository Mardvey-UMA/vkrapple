import { Modal } from 'antd'
import { useState } from 'react'
import { BurgerMenu } from '../../components/BurgerMenu/BurgerMenu'
import { SearchBar } from '../../components/SearchBar/SearchBar'
import styles from './SearchContainer.module.scss'

export const SearchContainer = () => {
	const [isModalOpen, setIsModalOpen] = useState(false)

	const handleSearch = (value: string) => {
		console.log('Search value:', value)
	}

	return (
		<div className={styles.container}>
			<SearchBar onSearch={handleSearch} />
			<BurgerMenu onClick={() => setIsModalOpen(true)} />

			<Modal
				title='Меню'
				open={isModalOpen}
				onCancel={() => setIsModalOpen(false)}
				footer={null}
				centered
			>
				<p>Содержимое меню будет добавлено позже</p>
			</Modal>
		</div>
	)
}
