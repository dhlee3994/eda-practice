package me.dhlee.edapractice.application.listener

import me.dhlee.edapractice.PaymentDeadLetterQueue
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentCreationFailedEventListener(
    private val paymentDeadLetterQueue: PaymentDeadLetterQueue,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun handlePaymentCreationFailed(event: PaymentCreationFailedEvent) {
        log.warn("Payment creation failed $event")
        try {
            paymentDeadLetterQueue.add(event, "Payment creation failed")
        } catch (e: Exception) {
            log.error("CRITICAL: Failed to add to DLQ - Event lost: $event", e)
            // 크리티컬 알람
        }
    }
}
