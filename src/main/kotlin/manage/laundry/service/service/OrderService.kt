package manage.laundry.service.service

import manage.laundry.service.dto.response.OrderResponse
import manage.laundry.service.entity.Order
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val staffRepository: StaffRepository,
    private val orderItemRepository: OrderItemRepository,
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository,
) {


    fun getActiveOrdersAssignedToStaff(staffId: Int): List<OrderResponse> {

        val excludedStatuses = listOf(
            Order.Status.COMPLETED,
            Order.Status.CANCELED,
            Order.Status.DELIVERED,
        )

        val orders = orderRepository.findAllActiveOrdersByStaffId(staffId, excludedStatuses)

        return orders.map {
            OrderResponse(
                id = it.id,
                shopName = it.shop.name,
                customerName = it.customer.name,
                estimatePrice = it.estimatePrice,
                totalPrice = it.totalPrice,
                status = it.status,
                specialInstructions = it.specialInstructions,
                createdAt = it.createdAt,
                items = orderItemRepository.findAllByOrderIdIn(it.id).map { item ->
                    OrderResponse.OrderItemResponse(
                        id = item.id,
                        name = item.shopService.name,
                        quantity = item.quantity,
                        price = item.price,
                        totalPrice = item.price * item.quantity
                    )
                },
                updateAt = it.updatedAt,
            )
        }
    }

    fun updateOrderStatus(orderId: Int, status: String, staffId: Int) {
        var order = orderRepository.findById(orderId)
            .orElseThrow { CustomException("Đơn hàng không tồn tại") }

        val validStatuses = Order.Status.entries.map { it.name }
        if (status !in validStatuses) {
            throw CustomException("Trạng thái không hợp lệ")
        }

        val staff = staffRepository.findByUserId(staffId)
            ?: throw CustomException("Nhân viên không tồn tại")

        if (order.shop.id != staff.shop.id) {
            throw CustomException("Bạn không có quyền cập nhật đơn hàng này")
        }

        order = order.copy(
            status = Order.Status.valueOf(status),
            updatedAt = LocalDateTime.now()
        )
        orderRepository.save(order)
    }

    fun getOrderHistory(customerId: Int): List<OrderResponse> {
        val customer = userRepository.findById(customerId)
            .orElseThrow { CustomException("Khách hàng không tồn tại") }

        val orders = orderRepository.findAllByCustomerId(customerId)

        return orders.map {
            OrderResponse(
                id = it.id,
                shopName = it.shop.name,
                totalPrice = it.totalPrice,
                status = it.status,
                createdAt = it.createdAt,
                customerName = customer.name,
                estimatePrice = it.estimatePrice,
                specialInstructions = it.specialInstructions,
                updateAt = it.updatedAt,
                items = orderItemRepository.findAllByOrderIdIn(it.id).map { item ->
                    OrderResponse.OrderItemResponse(
                        id = item.id,
                        name = item.shopService.name,
                        quantity = item.quantity,
                        price = item.price,
                        totalPrice = item.price * item.quantity
                    )
                }
            )
        }
    }


}