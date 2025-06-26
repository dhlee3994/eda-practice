package me.dhlee.edapractice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository: JpaRepository<Order, Long>