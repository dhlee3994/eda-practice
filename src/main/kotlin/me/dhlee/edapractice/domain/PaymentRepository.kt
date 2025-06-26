package me.dhlee.edapractice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository: JpaRepository<Payment, Long> {
    fun findAllByOrderId(orderId: Long): List<Payment>
}