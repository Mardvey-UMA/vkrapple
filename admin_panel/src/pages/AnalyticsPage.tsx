import { Column, Pie } from '@ant-design/plots'
import {
	Card,
	Col,
	Empty,
	InputNumber,
	message,
	Row,
	Segmented,
	Space,
	Spin,
	Statistic,
} from 'antd'
import { useState } from 'react'
import {
	useOrderStatus,
	useProductAnalytics,
	useSalesSummary,
} from '../hooks/admin/useAdminAnalytics'
type Mode = 'summary' | 'product' | 'status'
export default function AnalyticsPage() {
	const [mode, setMode] = useState<Mode>('summary')
	const [article, setArticle] = useState<number>()

	/* запросы */
	const summaryQ = useSalesSummary()
	const statusQ = useOrderStatus()
	const productQ = useProductAnalytics(article)

	/* ошибки */
	;[summaryQ, statusQ, productQ].forEach(q => {
		if (q.error) message.error((q.error as Error).message)
	})

	return (
		<Space
			direction='vertical'
			size='large'
			style={{ width: '100%', padding: 24 }}
		>
			<Segmented
				value={mode}
				onChange={val => setMode(val as Mode)}
				options={[
					{ label: 'Сводка', value: 'summary' },
					{ label: 'По товару', value: 'product' },
					{ label: 'Статусы заказов', value: 'status' },
				]}
			/>

			{mode === 'summary' &&
				(summaryQ.isLoading ? (
					<Spin />
				) : summaryQ.data ? (
					<>
						<Row gutter={16}>
							<Col span={8}>
								<Statistic title='Заказов' value={summaryQ.data.totalOrders} />
							</Col>
							<Col span={8}>
								<Statistic
									title='Единиц продано'
									value={summaryQ.data.totalUnitsSold}
								/>
							</Col>
							<Col span={8}>
								<Statistic
									title='Выручка, ₽'
									value={summaryQ.data.totalSalesAmount}
									precision={2}
								/>
							</Col>
						</Row>

						{/* Простейшая колонка – выручка в разрезе (заглушка примером) */}
						<Card title='Сравнение ключевых метрик'>
							<Column
								data={[
									{ kpi: 'Заказы', value: summaryQ.data.totalOrders },
									{ kpi: 'Единицы', value: summaryQ.data.totalUnitsSold },
									{
										kpi: 'Выручка, ₽',
										value: Number(summaryQ.data.totalSalesAmount),
									},
								]}
								xField='kpi'
								yField='value'
								height={300}
								xAxis={{ label: { autoHide: true } }}
							/>
						</Card>
					</>
				) : (
					<Empty />
				))}

			{mode === 'product' && (
				<>
					<InputNumber
						placeholder='Артикул товара'
						value={article}
						onChange={val => setArticle(val ?? undefined)}
						style={{ width: 200 }}
					/>

					{productQ.isFetching ? (
						<Spin />
					) : productQ.data ? (
						<Card title={`Товар ${productQ.data.productName}`}>
							<Column
								data={[
									{
										type: 'Шт',
										value: productQ.data.totalUnitsSold,
									},
									{
										type: '₽',
										value: Number(productQ.data.totalSalesAmount),
									},
								]}
								xField='type'
								yField='value'
								height={300}
							/>
						</Card>
					) : (
						<Empty description='Введите артикул' />
					)}
				</>
			)}

			{mode === 'status' &&
				(statusQ.isLoading ? (
					<Spin />
				) : statusQ.data && Object.keys(statusQ.data.statusCounts).length ? (
					<Card title='Заказы по статусам'>
						<Pie
							data={Object.entries(statusQ.data.statusCounts).map(
								([status, cnt]) => ({ status, cnt })
							)}
							angleField='cnt'
							colorField='status'
							radius={0.9}
							label={{ type: 'inner', offset: '-30%', content: '{percentage}' }}
							height={350}
						/>
					</Card>
				) : (
					<Empty description='Нет данных' />
				))}
		</Space>
	)
}
