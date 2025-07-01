package me.dhlee.edapractice.api

import me.dhlee.edapractice.PaymentDeadLetterQueue
import me.dhlee.edapractice.application.OrderService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PaymentDeadLetterProcessor(
    private val orderService: OrderService,
    private val paymentDeadLetterQueue: PaymentDeadLetterQueue
) {
    @Scheduled(fixedRate = 60_000L)
    fun processDeadLetterQueue() {
        val events = paymentDeadLetterQueue.pollBatch(10)
        events.forEach {
            val event = it.event
            orderService.restoreOrder(event.orderId)
        }
    }
}