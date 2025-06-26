package me.dhlee.edapractice.domain.event

data class OrderCreatedEvent(
    val orderId: Long,
    val orderAmount: Long,
)
