package me.dhlee.edapractice.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentServiceIntegrationTest {

    @Autowired
    private lateinit var paymentService: PaymentService

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @BeforeEach
    fun setUp() {
        paymentRepository.deleteAllInBatch()
    }

    @Test
    fun `결제 금액이 0이하이면 IllegalArgumentException 예외가 발생한다`() {
        val orderId = 1L
        val amount = 0L

        assertThatThrownBy { paymentService.createPayment(orderId, amount) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Invalid payment amount $amount")
    }

    @Test
    fun `동일한 주문에 대해서는 하나의 결제만 생성된다`() {
        val orderId = 1L
        val amount = 1000L

        val executorService = Executors.newFixedThreadPool(4)
        val futures = (1..4).map {
            executorService.submit { transactionTemplate.execute { paymentService.createPayment(orderId, amount) }}
        }
        futures.forEach { it.get() }
        executorService.shutdown()

        await().atMost(10, TimeUnit.SECONDS)
            .untilAsserted {
                val payments = paymentRepository.findAllByOrderId(orderId)
                assertThat(payments).hasSize(1)
            }
    }
}
