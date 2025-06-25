package me.dhlee.edapractice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "orders")
@Entity
class Order(
    @Column(nullable = false)
    val orderAmount: Long,
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.WAITING_FOR_PAYMENT,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}