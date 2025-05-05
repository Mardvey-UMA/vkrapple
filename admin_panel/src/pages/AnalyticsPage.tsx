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
import { useEffect, useMemo, useState } from 'react'
import {
	Bar,
	BarChart,
	Cell,
	Legend,
	Pie,
	PieChart,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis,
} from 'recharts'
import {
	useOrderStatus,
	useProductAnalytics,
	useSalesSummary,
} from '../hooks/admin/useAdminAnalytics'

type Mode = 'summary' | 'product' | 'status'

const KPI_COLORS = ['#1890ff', '#13c2c2', '#ffc53d']
const STATUS_COLORS = [
	'#52c41a',
	'#faad14',
	'#f5222d',
	'#722ed1',
	'#1890ff',
	'#8c8c8c',
]

export default function AnalyticsPage() {
	const [mode, setMode] = useState<Mode>('summary')
	const [article, setArticle] = useState<number>()

	/* hooks */
	const summaryQ = useSalesSummary()
	const statusQ = useOrderStatus()
	const productQ = useProductAnalytics(article)
	const { error: summaryError } = summaryQ
	const { error: statusError } = statusQ
	const { error: productError } = productQ
	useEffect(() => {
		;[summaryError, statusError, productError]
			.filter(Boolean)
			.forEach(err => message.error((err as Error).message))
	}, [summaryError, statusError, productError])

	/* memoized chart data */
	const summaryChartData = useMemo(
		() =>
			summaryQ.data
				? [
						{ kpi: 'Заказы', value: summaryQ.data.total_orders },
						{ kpi: 'Единицы', value: summaryQ.data.total_units_sold },
						{
							kpi: 'Выручка, ₽',
							value: Number(summaryQ.data.total_sales_amount),
						},
				  ]
				: [],
		[summaryQ.data]
	)

	const productChartData = useMemo(
		() =>
			productQ.data
				? [
						{ type: 'Шт', value: productQ.data.total_units_sold },
						{ type: '₽', value: Number(productQ.data.total_sales_amount) },
				  ]
				: [],
		[productQ.data]
	)

	/**
	 * Обратите внимание: status_counts может отсутствовать в ответе бекенда.
	 * Поэтому используем safe‑guard с nullish‑coalescing, чтобы исключить
	 * «can't convert undefined to object» при Object.entries().
	 */
	const status_counts = statusQ.data?.status_counts ?? {}

	const statusChartData = useMemo(
		() =>
			Object.entries(status_counts).map(([status, cnt]) => ({ status, cnt })),
		[status_counts]
	)

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

			{/* ------- Сводка ------- */}
			{mode === 'summary' &&
				(summaryQ.isLoading ? (
					<Spin />
				) : summaryQ.data ? (
					<>
						<Row gutter={16}>
							<Col span={8}>
								<Statistic title='Заказов' value={summaryQ.data.total_orders} />
							</Col>
							<Col span={8}>
								<Statistic
									title='Единиц продано'
									value={summaryQ.data.total_units_sold}
								/>
							</Col>
							<Col span={8}>
								<Statistic
									title='Выручка, ₽'
									value={summaryQ.data.total_sales_amount}
									precision={2}
								/>
							</Col>
						</Row>

						<Card title='Сравнение ключевых метрик'>
							<ResponsiveContainer width='100%' height={300}>
								<BarChart data={summaryChartData}>
									<XAxis dataKey='kpi' tick={{ fontSize: 12 }} />
									<YAxis allowDecimals={false} />
									<Tooltip />
									<Bar dataKey='value'>
										{summaryChartData.map((_, idx) => (
											<Cell
												key={`cell-${idx}`}
												fill={KPI_COLORS[idx % KPI_COLORS.length]}
											/>
										))}
									</Bar>
								</BarChart>
							</ResponsiveContainer>
						</Card>
					</>
				) : (
					<Empty />
				))}

			{/* ------- По товару ------- */}
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
						<Card title={`Товар ${productQ.data.product_name}`}>
							<ResponsiveContainer width='100%' height={300}>
								<BarChart data={productChartData}>
									<XAxis dataKey='type' tick={{ fontSize: 12 }} />
									<YAxis allowDecimals={false} />
									<Tooltip />
									<Bar dataKey='value' fill='#1890ff' />
								</BarChart>
							</ResponsiveContainer>
						</Card>
					) : (
						<Empty description='Введите артикул' />
					)}
				</>
			)}

			{/* ------- Статусы ------- */}
			{mode === 'status' &&
				(statusQ.isLoading ? (
					<Spin />
				) : statusChartData.length ? (
					<Card title='Заказы по статусам'>
						<ResponsiveContainer width='100%' height={350}>
							<PieChart>
								<Pie
									data={statusChartData}
									dataKey='cnt'
									nameKey='status'
									cx='50%'
									cy='50%'
									outerRadius='90%'
									label={({ percent }) => `${(percent * 100).toFixed(0)}%`}
								>
									{statusChartData.map((_, idx) => (
										<Cell
											key={`cell-${idx}`}
											fill={STATUS_COLORS[idx % STATUS_COLORS.length]}
										/>
									))}
								</Pie>
								<Legend />
								<Tooltip />
							</PieChart>
						</ResponsiveContainer>
					</Card>
				) : (
					<Empty description='Нет данных' />
				))}
		</Space>
	)
}
