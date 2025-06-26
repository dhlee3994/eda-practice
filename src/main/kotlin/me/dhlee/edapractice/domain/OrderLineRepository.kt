package me.dhlee.edapractice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface OrderLineRepository: JpaRepository<OrderLine, Long> {
    fun findAllByOrderId(orderId: Long): List<OrderLine>
}