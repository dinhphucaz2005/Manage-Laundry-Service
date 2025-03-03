package manage.laundry.service.service

import jakarta.transaction.Transactional
import manage.laundry.service.dto.request.CreateOrderRequest
import manage.laundry.service.dto.request.TrackOrderResponse
import manage.laundry.service.dto.request.UpdateOrderRequest
import manage.laundry.service.dto.response.CreateOrderResponse
import manage.laundry.service.entity.Order
import manage.laundry.service.entity.OrderItem
import manage.laundry.service.repository.*
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CustomerOrderService(
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
    private val shopServiceRepository: ShopServiceRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) {

    fun createOrder(customerId: Int, request: CreateOrderRequest): CreateOrderResponse {
        val customer = userRepository.findById(customerId)
            .orElseThrow { Exception("Khách hàng không tồn tại") }

        val shop = shopRepository.findById(request.shopId)
            .orElseThrow { Exception("Tiệm không tồn tại") }

        var totalPrice = BigDecimal.ZERO

        val order = orderRepository.save(
            Order(
                customer = customer,
                shop = shop,
                specialInstructions = request.specialInstructions,
                totalPrice = BigDecimal.ZERO // tạm, lát cập nhật lại
            )
        )

        request.items.forEach { item ->
            val service = shopServiceRepository.findById(item.serviceId)
                .orElseThrow { Exception("Dịch vụ không tồn tại") }

            if (service.shop.id != shop.id) {
                throw Exception("Dịch vụ không thuộc tiệm này")
            }

            val itemPrice = service.price.multiply(BigDecimal.valueOf(item.quantity.toLong()))
            totalPrice += itemPrice

            orderItemRepository.save(
                OrderItem(
                    order = order,
                    shopService = service,
                    quantity = item.quantity,
                    price = itemPrice
                )
            )
        }

        orderRepository.save(order.copy(totalPrice = totalPrice))

        return CreateOrderResponse(
            orderId = order.id,
            totalPrice = totalPrice,
            status = order.status.name,
            createdAt = order.createdAt
        )
    }


    fun trackOrder(orderId: Int, customerId: Int): TrackOrderResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { Exception("Đơn hàng không tồn tại") }

        if (order.customer.id != customerId) {
            throw Exception("Bạn không có quyền xem đơn hàng này")
        }

        return TrackOrderResponse(
            orderId = order.id,
            shopName = order.shop.name,
            status = order.status,
            totalPrice = order.totalPrice,
            specialInstructions = order.specialInstructions,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )
    }

    @Transactional
    fun updateOrderByOwner(orderId: Int, request: UpdateOrderRequest, ownerId: Int) {
        var order = orderRepository.findById(orderId)
            .orElseThrow { Exception("Đơn hàng không tồn tại") }

        if (order.shop.owner.id != ownerId) {
            throw Exception("Bạn không có quyền cập nhật đơn hàng này")
        }

        order = order.copy(
            status = request.status,
            specialInstructions = request.specialInstructions,
        )

        orderRepository.save(order)
    }

}
