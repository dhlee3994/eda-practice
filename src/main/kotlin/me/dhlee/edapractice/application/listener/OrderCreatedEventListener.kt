package me.dhlee.edapractice.application.listener

import me.dhlee.edapractice.domain.PaymentService
import me.dhlee.edapractice.domain.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCreatedEventListener(
    private val paymentService: PaymentService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(value = [OrderCreatedEvent::class], phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCreatedEvent(event: OrderCreatedEvent) {
        log.info("Order created event: $event")
        paymentService.createPayment(event.orderId, event.orderAmount)
    }
}