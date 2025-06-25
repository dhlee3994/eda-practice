package me.dhlee.edapractice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "order_lines")
@Entity
class OrderLine(
    @Column(nullable = false)
    val orderId: Long,
    @Column(nullable = false)
    val productId: Long,
    @Column(nullable = false)
    val productName: String,
    @Column(nullable = false)
    val price: Long,
    @Column(nullable = false)
    val quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}