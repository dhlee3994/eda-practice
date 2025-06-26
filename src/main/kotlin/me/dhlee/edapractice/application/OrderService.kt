package me.dhlee.edapractice.application

import me.dhlee.edapractice.domain.Order
import me.dhlee.edapractice.domain.OrderLine
import me.dhlee.edapractice.domain.OrderLineRepository
import me.dhlee.edapractice.domain.OrderRepository
import me.dhlee.edapractice.domain.Payment
import me.dhlee.edapractice.domain.PaymentRepository
import me.dhlee.edapractice.domain.PriceRepository
import me.dhlee.edapractice.domain.ProductRepository
import me.dhlee.edapractice.domain.StockRepository
import me.dhlee.edapractice.dto.OrderRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val productRepository: ProductRepository,
    private val stockRepository: StockRepository,
    private val priceRepository: PriceRepository,
    private val orderRepository: OrderRepository,
    private val orderLineRepository: OrderLineRepository,
    private val paymentRepository: PaymentRepository,
) {
    @Transactional
    fun processOrder(request: List<OrderRequest>):Long {
        val sortedRequest = request.sortedBy { it.productId }
        val productIds = sortedRequest.map { it.productId }.sorted()
        val products = productRepository.findAllById(productIds)
        if (products.size != sortedRequest.size) {
            throw IllegalArgumentException("주문할 수 없는 상품이 있습니다. 확인해주세요.")
        }

        val stockMap = stockRepository.findAllByProductIdIn(productIds).associateBy { it.productId }
        sortedRequest.forEach{request ->
            stockMap[request.productId]
                ?.decrease(request.quantity)
                ?: throw IllegalArgumentException("상품의 재고 정보가 없습니다.")
        }

        val priceMap = priceRepository.findAllByProductIdIn(productIds).associateBy { it.productId }
        val orderAmount = sortedRequest.sumOf { request ->
            val productId = request.productId
            val price = priceMap[productId] ?: throw IllegalArgumentException("상품의 가격 정보가 없습니다.")
            request.quantity * price.amount
        }
        val order = orderRepository.save(Order(orderAmount))

        val productMap = products.associateBy { it.id!! }
        val orderId = order.id!!
        sortedRequest.forEach { request ->
            val productId = request.productId
            val price = priceMap[productId] ?: throw IllegalArgumentException("상품의 가격 정보가 없습니다.")
            orderLineRepository.save(
                OrderLine(
                    orderId = orderId,
                    productId = request.productId,
                    productName = productMap[productId]!!.name,
                    price = price.amount,
                    quantity = request.quantity
                )
            )
        }

        paymentRepository.save(Payment(orderId, orderAmount))
        return orderId
    }
}