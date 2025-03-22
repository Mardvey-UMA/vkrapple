package ru.webshop.backend.controller

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.ProductCreateRequestDTO
import ru.webshop.backend.dto.ProductPageResponseDTO
import ru.webshop.backend.entity.Category
import ru.webshop.backend.service.interfaces.ProductService
import ru.webshop.backend.spec.ProductSpecifications

@RestController
@RequestMapping("/api/products")
class ProductsController(
    private val productService: ProductService
) {
    private val logger = LoggerFactory.getLogger(ProductsController::class.java)

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequestDTO
    ) = productService.createProduct(request)

    @GetMapping
    fun getAllProducts(
        @PageableDefault(size = 20, sort = ["name"]) pageable: Pageable
    ) = productService.getAllProducts(pageable)

    @GetMapping("/category/{categoryId}")
    fun getByCategory(
        @PathVariable categoryId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ) = productService.getProductsByCategory(categoryId, pageable)

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