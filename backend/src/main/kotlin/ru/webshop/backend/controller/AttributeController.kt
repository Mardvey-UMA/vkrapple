package ru.webshop.backend.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.service.interfaces.AttributeService

@RestController
@RequestMapping("/attribute")
class AttributeController(
    private val attributeService: AttributeService
) {

}