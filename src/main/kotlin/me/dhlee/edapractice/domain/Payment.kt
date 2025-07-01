package me.dhlee.edapractice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "payments")
@Entity
class Payment(
    @Column(nullable = false, unique = true)
    val orderId: Long,
    @Column(nullable = false)
    val amount: Long,
    @Column(nullable = false)
    var status: PaymentStatus = PaymentStatus.WAITING,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}