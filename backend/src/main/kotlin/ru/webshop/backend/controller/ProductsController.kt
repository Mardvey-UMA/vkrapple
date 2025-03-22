package ru.webshop.backend.controller

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.ProductCreateRequestDTO
import ru.webshop.backend.entity.Category
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/products")
class ProductsController(
    private val productService: ProductService
) {
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
        @RequestParam categoryId: Long,
        @RequestParam attributes: Map<Long, String>,
        @PageableDefault(size = 20) pageable: Pageable
    ) = productService.searchProducts(categoryId, attributes, pageable)
}