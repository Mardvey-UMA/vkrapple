package ru.webshop.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import ru.webshop.backend.entity.Role
import ru.webshop.backend.repository.RoleRepository
import ru.webshop.backend.service.interfaces.AttributeService
import ru.webshop.backend.service.interfaces.CategoryService

@SpringBootApplication
@EnableJpaAuditing
class BackendApplication {
    var logger: Logger = LoggerFactory.getLogger(this::class.java)
    @Bean
    fun runner(
        roleRepository: RoleRepository, // Плохо(
        categoryService: CategoryService,
        attributeService: AttributeService
    ): CommandLineRunner {
        return CommandLineRunner {
            // Создание ролей
            listOf(
                Role(roleName = "USER", description = "Usual user"),
                Role(roleName = "ADMIN", description = "Admin can add products")
            ).forEach { role ->
                if (!roleRepository.existsByRoleName(role.roleName)) {
                    roleRepository.save(role)
                }
            }
            // Создание категорий и атрибутов
            val categoriesToCreate = listOf(
                "Ручка" to listOf("Производитель", "Толщина стержня", "Модель", "Цвет", "Кол-во в упаковке"),
                "Тетрадь" to listOf("Производитель", "Линовка", "Кол-во в упаковке", "Кол-во страниц"),
                "Ластик" to listOf("Производитель", "Кол-во в упаковке", "Твердость", "Тип ластика", "Цвет"),
                "Карандаш" to listOf("Производитель", "Твердость", "Цвет", "Кол-во в упаковке", "Ластик на конце"),
                "Фломастер" to listOf("Производитель", "Кол-во в упаковке", "Кол-во цветов", "Тип краски"),
            )

            categoriesToCreate.forEach { (categoryName, attributes) ->
                val category = try {
                    categoryService.createCategory(categoryName)
                } catch (e: RuntimeException) {
                    categoryService.getByCategoryName(categoryName)
                }

                attributes.forEach { attributeName ->
                    try {
                        //logger.info("Adding attribute {} to category {}", attributeName, category.id)
                        attributeService.createAttributeToCategory(category.id, attributeName)
                    } catch (_: RuntimeException) {
                    }
                }
            }
        }
    }
}
fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
