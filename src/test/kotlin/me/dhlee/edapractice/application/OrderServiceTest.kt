package me.dhlee.edapractice.application

import me.dhlee.edapractice.domain.OrderLineRepository
import me.dhlee.edapractice.domain.OrderRepository
import me.dhlee.edapractice.domain.PaymentRepository
import me.dhlee.edapractice.domain.Price
import me.dhlee.edapractice.domain.PriceRepository
import me.dhlee.edapractice.domain.Product
import me.dhlee.edapractice.domain.ProductRepository
import me.dhlee.edapractice.domain.Stock
import me.dhlee.edapractice.domain.StockRepository
import me.dhlee.edapractice.dto.OrderRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var stockRepository: StockRepository

    @Autowired
    private lateinit var priceRepository: PriceRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderLineRepository: OrderLineRepository

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    @BeforeEach
    fun setUp() {
        paymentRepository.deleteAllInBatch()
        orderLineRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
        stockRepository.deleteAllInBatch()
        priceRepository.deleteAllInBatch()
        productRepository.deleteAllInBatch()
    }

    @Test
    fun `주문 성공`() {
        val product1 = productRepository.save(Product(name = "노트북"))
        val product2 = productRepository.save(Product(name = "마우스"))

        stockRepository.save(Stock(productId = product1.id!!, quantity = 10))
        stockRepository.save(Stock(productId = product2.id!!, quantity = 20))

        priceRepository.save(Price(productId = product1.id!!, amount = 1000))
        priceRepository.save(Price(productId = product2.id!!, amount = 2000))

        val orderRequests = listOf(
            OrderRequest(productId = product1.id!!, quantity = 2),
            OrderRequest(productId = product2.id!!, quantity = 3)
        )

        val orderId = orderService.processOrder(orderRequests)

        // 주문 확인
        val savedOrder = orderRepository.findById(orderId).orElseThrow()
        assertThat(savedOrder.orderAmount).isEqualTo(8000) // (1000 * 2) + (2000 * 3)

        // 주문 아이템 확인
        val orderLines = orderLineRepository.findAllByOrderId(orderId)
        assertThat(orderLines).hasSize(2)
        assertThat(orderLines[0].productName).isEqualTo("노트북")
        assertThat(orderLines[0].quantity).isEqualTo(2)
        assertThat(orderLines[0].price).isEqualTo(1000)
        assertThat(orderLines[1].productName).isEqualTo("마우스")
        assertThat(orderLines[1].quantity).isEqualTo(3)
        assertThat(orderLines[1].price).isEqualTo(2000)

        // 결제 확인
        val payments = paymentRepository.findAllByOrderId(orderId)
        assertThat(payments).hasSize(1)
        assertThat(payments[0].amount).isEqualTo(8000)

        // 재고 감소 확인
        val updatedStock1 = stockRepository.findByProductId(product1.id!!).orElseThrow()
        val updatedStock2 = stockRepository.findByProductId(product2.id!!).orElseThrow()
        assertThat(updatedStock1.quantity).isEqualTo(8) // 10 - 2
        assertThat(updatedStock2.quantity).isEqualTo(17) // 20 - 3
    }

    @Test
    fun `주문 실패 - 재고부족`() {
        val product = productRepository.save(Product(name = "노트북"))
        val productId = product.id!!
        stockRepository.save(Stock(productId = productId, quantity = 1))
        priceRepository.save(Price(productId = productId, amount = 1000))
        val orderRequests = listOf(
            OrderRequest(productId = productId, quantity = 2),
        )

        assertThatThrownBy { orderService.processOrder(orderRequests) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("재고가 부족합니다.")

        assertThat(orderRepository.findAll()).hasSize(0)
        assertThat(orderLineRepository.findAll()).hasSize(0)
        assertThat(paymentRepository.findAll()).hasSize(0)
        assertThat(stockRepository.findByProductId(productId).orElseThrow().quantity).isEqualTo(1)
    }
}
