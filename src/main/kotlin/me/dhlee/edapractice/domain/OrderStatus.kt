package me.dhlee.edapractice.domain

enum class OrderStatus {
    WAITING_FOR_PAYMENT,
    PAYMENT_FAILED,
    PAID,
    PREPARE_SHIPMENT,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUND_REQUESTED,
    REFUNDED,
}
