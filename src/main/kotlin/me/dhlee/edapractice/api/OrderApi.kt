package me.dhlee.edapractice.api

import me.dhlee.edapractice.application.OrderService
import me.dhlee.edapractice.dto.OrderRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/orders")
@RestController
class OrderApi(
    private val orderService: OrderService,
) {
    @PostMapping
    fun processOrder(
        @RequestBody request: List<OrderRequest>,
    ): Long {
        return orderService.processOrder(request)
    }
}