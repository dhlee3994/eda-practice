package me.dhlee.edapractice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "stock")
@Entity
class Stock(
    @Column(nullable = false)
    val productId : Long,
    @Column(nullable = false)
    var quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    init {
        validate()
    }

    fun validate() {
        if (quantity < 0) {
            throw IllegalArgumentException("재고가 부족합니다.")
        }
    }

    fun decrease(quantity: Long) {
        this.quantity -= quantity
        validate()
    }

    fun increase(quantity: Long) {
        this.quantity += quantity
        validate()
    }
}