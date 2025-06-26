package me.dhlee.edapractice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long>