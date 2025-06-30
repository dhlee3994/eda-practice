package me.dhlee.edapractice.application.listener

import me.dhlee.edapractice.domain.PaymentService
import me.dhlee.edapractice.domain.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCreatedEventListener(
    private val paymentService: PaymentService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Retryable(
        value = [Exception::class],
        exclude = [IllegalArgumentException::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0, random = true)
    )
    @Async
    @TransactionalEventListener(value = [OrderCreatedEvent::class], phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCreatedEvent(event: OrderCreatedEvent) {
        log.info("Order created event: $event")
        paymentService.createPayment(event.orderId, event.orderAmount)
        log.info("Payment created successfully for order: ${event.orderId}")
    }

    @Recover
    fun recover(e: Exception, event: OrderCreatedEvent) {
        log.error("Failed to create payment for order: ${event.orderId}", e)
        eventPublisher.publishEvent(
            PaymentCreationFailedEvent(
                event.orderId,
                e.message ?: "Unknown error"
            )
        )
    }
}