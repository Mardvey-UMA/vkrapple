import { PlusOutlined } from '@ant-design/icons'
import { useQuery } from '@tanstack/react-query'
import {
	Button,
	Card,
	Form,
	Input,
	InputNumber,
	message,
	Select,
	Space,
	Typography,
	Upload,
	UploadFile,
} from 'antd'
import { RcFile } from 'antd/es/upload'
import { useEffect, useMemo, useState } from 'react'
import { CategoryService } from '../api/category'
import {
	useCreateProduct,
	useUploadPhoto,
} from '../hooks/admin/useAdminProducts'
import { ProductCreateRequestDTO } from '../types/product'
const { Title } = Typography

export default function CreateProductPage() {
	/* ─────────── загрузка категорий ─────────── */
	const { data: categoriesResp } = useQuery({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})
	const categoriesOptions = useMemo(
		() =>
			categoriesResp
				? Object.entries(categoriesResp.categories).map(([name, id]) => ({
						label: name,
						value: id,
				  }))
				: [],
		[categoriesResp]
	)

	/* ─────────── реактивные атрибуты ─────────── */
	const [selectedCategoryId, setSelectedCategoryId] = useState<number>()
	const {
		data: attrsResp,
		refetch: refetchAttrs,
		isFetching: attrsLoading,
	} = useQuery({
		queryKey: ['category-attrs', selectedCategoryId],
		queryFn: () => CategoryService.getAttributes(selectedCategoryId!),
		enabled: false,
	})
	useEffect(() => {
		if (selectedCategoryId) refetchAttrs()
	}, [selectedCategoryId, refetchAttrs])

	/* ─────────── снимок файлов ─────────── */
	const [fileList, setFileList] = useState<UploadFile<RcFile>[]>([])

	const beforeUpload = (file: RcFile) => {
		if (fileList.length >= 5) {
			message.warning('Максимум 5 фото')
			return Upload.LIST_IGNORE
		}
		setFileList(prev => [...prev, file])
		return false // отменяем авто-загрузку
	}

	const removeFile = (file: UploadFile) => {
		setFileList(prev => prev.filter(f => f.uid !== file.uid))
	}

	/* ─────────── мутации ─────────── */
	const createMut = useCreateProduct()
	const uploadMut = useUploadPhoto()

	// src/pages/CreateProductPage.tsx
	const onFinish = async (values: Record<string, unknown>) => {
		try {
			const attrMap: Record<string, string> = {}
			attrsResp?.attributes.forEach(a => {
				const v = values[`attr_${a.id}`] as string | undefined
				if (v) attrMap[a.id.toString()] = v
			})

			const dto: ProductCreateRequestDTO = {
				name: values.name as string,
				price: Number(values.price),
				balanceInStock: Number(values.balance),
				description: values.description as string | undefined,
				categoryId: selectedCategoryId!,
				attributes: attrMap,
				numberOfOrders: 0,
				rating: 0,
			}

			const article = await createMut.mutateAsync(dto)

			/* upload photos */
			await Promise.all(
				fileList.map((f, i) =>
					uploadMut.mutateAsync({
						meta: { articleNumber: article, indexNumber: i + 1 },
						file: f as RcFile,
					})
				)
			)

			message.success(`Товар ${article} создан!`)
			form.resetFields()
			setFileList([])
		} catch (e) {
			message.error((e as Error).message)
		}
	}

	const [form] = Form.useForm()

	return (
		<div style={{ padding: 24, maxWidth: 800 }}>
			<Title level={3}>Новый товар</Title>

			<Card>
				<Form
					layout='vertical'
					form={form}
					onFinish={onFinish}
					disabled={createMut.isPending || uploadMut.isPending}
				>
					<Form.Item
						name='categoryId'
						label='Категория'
						rules={[{ required: true, message: 'Выберите категорию' }]}
					>
						<Select
							options={categoriesOptions}
							onChange={val => setSelectedCategoryId(val)}
							placeholder='—'
						/>
					</Form.Item>

					<Form.Item
						name='name'
						label='Название'
						rules={[{ required: true, min: 3 }]}
					>
						<Input />
					</Form.Item>

					<Form.Item
						name='price'
						label='Цена, ₽'
						rules={[{ required: true, type: 'number', min: 0 }]}
					>
						<InputNumber style={{ width: '100%' }} />
					</Form.Item>

					<Form.Item
						name='balance'
						label='Количество на складе'
						rules={[{ required: true, type: 'number', min: 0 }]}
					>
						<InputNumber style={{ width: '100%' }} />
					</Form.Item>

					<Form.Item name='description' label='Описание'>
						<Input.TextArea rows={3} />
					</Form.Item>

					{/* динамические поля атрибутов */}
					{attrsLoading && <p>Загрузка атрибутов…</p>}
					{attrsResp?.attributes.map(attr => (
						<Form.Item
							key={attr.id}
							name={`attr_${attr.id}`}
							label={attr.name}
							rules={[{ required: true }]}
						>
							{attr.values.length ? (
								<Select
									options={attr.values.map(v => ({ label: v, value: v }))}
									placeholder='—'
								/>
							) : (
								<Input />
							)}
						</Form.Item>
					))}

					{/* фото */}
					<Form.Item
						label='Фотографии (до 5 шт)'
						required
						validateStatus={!fileList.length ? 'error' : ''}
						help={!fileList.length && 'Добавьте минимум одну фотографию'}
					>
						<Upload
							listType='picture-card'
							fileList={fileList}
							beforeUpload={beforeUpload}
							onRemove={removeFile}
							accept='image/*'
						>
							{fileList.length >= 5 ? null : (
								<div>
									<PlusOutlined />
									<div style={{ marginTop: 8 }}>Добавить</div>
								</div>
							)}
						</Upload>
					</Form.Item>

					<Space direction='vertical' style={{ width: '100%' }}>
						<Button
							type='primary'
							htmlType='submit'
							block
							loading={createMut.isPending || uploadMut.isPending}
							disabled={!fileList.length}
						>
							Сохранить
						</Button>
					</Space>
				</Form>
			</Card>
		</div>
	)
}
