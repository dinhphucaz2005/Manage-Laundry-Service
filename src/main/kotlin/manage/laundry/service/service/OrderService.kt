package manage.laundry.service.service

import manage.laundry.service.dto.response.OrderHistoryResponse
import manage.laundry.service.dto.response.OrderResponse
import manage.laundry.service.entity.Order
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.OrderRepository
import manage.laundry.service.repository.StaffRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val staffRepository: StaffRepository
) {


    fun getActiveOrdersAssignedToStaff(staffId: Int): List<OrderResponse> {

        val excludedStatuses = listOf(
            Order.Status.COMPLETED,
            Order.Status.CANCELLED
        )

        val orders = orderRepository.findAllActiveOrdersByStaffId(staffId, excludedStatuses)

        return orders.map {
            OrderResponse(
                id = it.id,
                shopName = it.shop.name,
                customerName = it.customer.name,
                totalPrice = it.totalPrice,
                status = it.status,
                specialInstructions = it.specialInstructions,
                createdAt = it.createdAt
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

    fun getOrderHistory(customerId: Int): List<OrderHistoryResponse> {
        val orders = orderRepository.findAllByCustomerId(customerId)

        return orders.map {
            OrderHistoryResponse(
                orderId = it.id,
                shopName = it.shop.name,
                totalPrice = it.totalPrice,
                status = it.status.name,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }


}