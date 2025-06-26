package me.dhlee.edapractice.domain

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface StockRepository: JpaRepository<Stock, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.productId in :productIds")
    fun findAllByProductIdIn(productIds: List<Long>): List<Stock>

    fun findByProductId(id: Long): Optional<Stock>
}