package me.dhlee.edapractice.application.listener

import me.dhlee.edapractice.application.OrderService
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentCreationFailedEventListener(
    private val orderService: OrderService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handlePaymentCreationFailed(event: PaymentCreationFailedEvent) {
        log.warn("Payment creation failed $event")

        try {
            orderService.restoreOrder(event.orderId)
        } catch (e: Exception) {
            log.error("Failed to restore order for orderId: ${event.orderId}", e)
            // 크리티컬 알람
        }
    }
}