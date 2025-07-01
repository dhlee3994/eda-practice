package me.dhlee.edapractice

import me.dhlee.edapractice.application.listener.PaymentCreationFailedEvent
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentLinkedQueue

@Component
class PaymentDeadLetterQueue {
    private val queue = ConcurrentLinkedQueue<FailedEvent>()

    fun add(event: PaymentCreationFailedEvent, error: String) {
        queue.offer(FailedEvent(event, error, LocalDateTime.now()))
    }

    fun pollBatch(size: Int): List<FailedEvent> {
        return (1..size).mapNotNull { queue.poll() }
    }

    fun size() = queue.size
    fun clear() = queue.clear()
}

data class FailedEvent(
    val event: PaymentCreationFailedEvent,
    val error: String,
    val timestamp: LocalDateTime
)