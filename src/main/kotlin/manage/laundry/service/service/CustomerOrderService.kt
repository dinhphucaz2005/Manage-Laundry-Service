package manage.laundry.service.service

import jakarta.transaction.Transactional
import manage.laundry.service.dto.request.CreateOrderRequest
import manage.laundry.service.dto.request.UpdateOrderRequest
import manage.laundry.service.dto.response.CreateOrderResponse
import manage.laundry.service.dto.response.OrderResponse
import manage.laundry.service.entity.Order
import manage.laundry.service.entity.OrderItem
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.*
import org.springframework.stereotype.Service

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
            .orElseThrow { CustomException("Khách hàng không tồn tại") }

        val shop = shopRepository.findById(request.shopId)
            .orElseThrow { CustomException("Tiệm không tồn tại") }


        val shopServices = shopServiceRepository.findByShop(shop)
        var estimatePrice = 0


        val order = orderRepository.save(
            Order(
                customer = customer,
                shop = shop,
                specialInstructions = request.specialInstructions,
                estimatePrice = request.estimatePrice(shopServices),
                status = Order.Status.NEW,
            )
        )

        request.items.forEach { item ->
            val service = shopServiceRepository.findById(item.serviceId)
                .orElseThrow { CustomException("Dịch vụ không tồn tại") }

            if (service.shop.id != shop.id) {
                throw CustomException("Dịch vụ không thuộc tiệm này")
            }

            val itemPrice = service.price * item.quantity
            estimatePrice += itemPrice

            orderItemRepository.save(
                OrderItem(
                    order = order,
                    shopService = service,
                    quantity = item.quantity,
                    price = itemPrice
                )
            )
        }

        orderRepository.save(order.copy(totalPrice = estimatePrice))

        return CreateOrderResponse(
            orderId = order.id,
            estimatePrice = estimatePrice,
            status = order.status.name,
            createdAt = order.createdAt
        )
    }


    fun trackOrder(orderId: Int, customerId: Int): OrderResponse {
        val order = orderRepository.findById(orderId)
            .orElseThrow { CustomException("Đơn hàng không tồn tại") }

        if (order.customer.id != customerId) {
            throw CustomException("Bạn không có quyền xem đơn hàng này")
        }

        val customer = userRepository.findById(customerId)
            .orElseThrow { CustomException("Khách hàng không tồn tại") }

        return OrderResponse(
            id = order.id,
            shopName = order.shop.name,
            status = order.status,
            totalPrice = order.totalPrice,
            specialInstructions = order.specialInstructions,
            estimatePrice = order.estimatePrice,
            createdAt = order.createdAt,
            items = orderItemRepository.findAllByOrderIdIn(orderId).map {
                OrderResponse.OrderItemResponse(
                    id = it.id,
                    name = it.shopService.name,
                    quantity = it.quantity,
                    price = it.price,
                    totalPrice = it.price * it.quantity
                )
            },
            customerName = customer.name,
            updateAt = order.updatedAt,
        )
    }

    @Transactional
    fun updateOrderByOwner(orderId: Int, request: UpdateOrderRequest, ownerId: Int) {
        var order = orderRepository.findById(orderId)
            .orElseThrow { CustomException("Đơn hàng không tồn tại") }

        if (order.shop.owner.id != ownerId) {
            throw CustomException("Bạn không có quyền cập nhật đơn hàng này")
        }

        order = order.copy(
            status = request.status,
            specialInstructions = request.specialInstructions,
        )

        orderRepository.save(order)
    }

}
