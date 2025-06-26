package me.dhlee.edapractice.dto

data class OrderRequest(
    val productId: Long,
    val quantity: Long,
)