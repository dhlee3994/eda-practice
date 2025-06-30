package me.dhlee.edapractice.application.listener

data class PaymentCreationFailedEvent(
    val orderId: Long,
    val reason: String,
)
