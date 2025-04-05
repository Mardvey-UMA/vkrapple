import { SearchOutlined } from '@ant-design/icons'
import { Input } from 'antd'
import styles from './SearchBar.module.scss'

type SearchBarProps = {
	onSearch: (value: string) => void
}

export const SearchBar = ({ onSearch }: SearchBarProps) => {
	return (
		<Input
			placeholder='Поиск товаров...'
			prefix={<SearchOutlined className={styles.icon} />}
			className={styles.searchInput}
			onChange={e => onSearch(e.target.value)}
			allowClear
		/>
	)
}
