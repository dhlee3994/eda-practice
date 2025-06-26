package me.dhlee.edapractice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PriceRepository: JpaRepository<Price, Long> {
    fun findAllByProductIdIn(productIds: List<Long>): List<Price>
}