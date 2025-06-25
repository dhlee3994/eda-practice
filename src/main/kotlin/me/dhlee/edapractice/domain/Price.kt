package me.dhlee.edapractice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "prices")
@Entity
class Price(
    @Column(nullable = false)
    val productId: Long,
    
    @Column(nullable = false)
    val amount: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}