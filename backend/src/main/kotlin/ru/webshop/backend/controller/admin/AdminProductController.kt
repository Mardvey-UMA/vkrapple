package ru.webshop.backend.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/admin/products")
@Tag(name = "AdminProducts", description = "CRUD-операции с товарами (админ)")
class AdminProductController(
    private val productService: ProductService
) {

    @Operation(summary = "Удалить товар по артикулу")
    @DeleteMapping("/{articleNumber}")
    fun delete(@PathVariable articleNumber: Long): ResponseEntity<Void> {
        productService.deleteProduct(articleNumber)
        return ResponseEntity.noContent().build()
    }
}
