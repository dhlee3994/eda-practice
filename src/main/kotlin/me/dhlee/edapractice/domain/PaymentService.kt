package me.dhlee.edapractice.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService (
    private val paymentRepository: PaymentRepository,
){
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createPayment(orderId: Long, amount: Long) {
        log.info("Creating payment $orderId, $amount")
        if (amount <= 0) {
            throw IllegalArgumentException("Invalid payment amount $amount")
        }
        paymentRepository.save(Payment(orderId, amount))
    }
}