package ru.webshop.backend.controller
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.ProductCreateRequestDTO
import ru.webshop.backend.dto.ProductPageResponseDTO
import ru.webshop.backend.dto.ProductResponseDTO
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Управление товарами")
class ProductsController(
    private val productService: ProductService
) {
    @Operation(summary = "Создать товар",
        description = "Создать товар с указанными атрибутами и значениями и категорией (id)")
    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequestDTO)
    = productService.createProduct(request)

    @Operation(summary = "Информация о конкретном товаре",
        description = "Получение подробной информации о товаре по артикулу (отзывы, фото и т.д.)")
    @GetMapping("/{article}")
    fun getProduct(@PathVariable article: Long) : ProductResponseDTO
        = productService.getProduct(article)

    @Operation(summary = "Получить все товары",
        description = "Список всех товаров с пагинацией")
    @GetMapping
    fun getAllProducts(
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable)
    = productService.getAllProducts(pageable)


    @Operation(summary = "Товары по категории",
        description = "Список всех товаров из конкретной категории по её id")
    @GetMapping("/category/{categoryId}")
    fun getByCategory(
        @PathVariable categoryId: Long,
        @PageableDefault(size = 20) pageable: Pageable)
    = productService.getProductsByCategory(categoryId, pageable)

    @Operation(summary = "Поиск товаров с фильтром",
        description = "Поиск товаров в конкретной категории с возможными значениями атрибутов")
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam("categoryId") categoryId: Long,
        @RequestParam(required = false)
        params: MultiValueMap<String, String>,
        @PageableDefault(size = 20) pageable: Pageable
    ): ProductPageResponseDTO {

        val attributes = params
            .filterKeys { it.startsWith("attributes[") }
            .mapKeys {
                it.key
                    .removePrefix("attributes[")
                    .removeSuffix("]")
                    .toLongOrNull()
            }
            .filterKeys { it != null }
            .mapValues { (_, values) ->
                values.filter { value -> value.isNotBlank() }
            }
            .filterValues { it.isNotEmpty() }
            .mapKeys { it.key!! }

        return productService.searchProducts(categoryId, attributes, pageable)
    }
}