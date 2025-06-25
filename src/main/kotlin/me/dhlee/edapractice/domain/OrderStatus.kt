package me.dhlee.edapractice.domain

enum class OrderStatus {
    WAITING_FOR_PAYMENT,
    PAID,
    PREPARE_SHIPMENT,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUND_REQUESTED,
    REFUNDED,
}
