package manage.laundry.service.service

import manage.laundry.service.dto.request.CancelOrderRequest
import manage.laundry.service.dto.request.ConfirmOrderRequest
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
    private val userRepository: UserRepository,
    private val firebaseNotificationService: FirebaseNotificationService
) {

    fun getOrdersByStatus(status: Order.Status, staffId: Int): List<OrderResponse> {
        val staff = staffRepository.findByUserId(staffId)
            ?: throw CustomException("Nhân viên không tồn tại")

        val orders = orderRepository.findAllByStatusAndShopId(status, staff.shop.id)

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

    fun getActiveOrdersAssignedToStaff(staffId: Int): List<OrderResponse> {

        val excludedStatuses = listOf(
            Order.Status.COMPLETED,
            Order.Status.CANCELED,
            Order.Status.DELIVERED,
            Order.Status.PAID,
//            Order.Status.PAID_FAILED,
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

        val newStatus = Order.Status.valueOf(status)
        order = order.copy(
            status = newStatus,
            updatedAt = LocalDateTime.now()
        )
        val updatedOrder = orderRepository.save(order)

        // Create a user-friendly message based on status
        val message = when (newStatus) {
            Order.Status.PROCESSING -> "Đơn hàng của bạn đang được xử lý"
            Order.Status.COMPLETED -> "Đơn hàng của bạn đã hoàn thành"
            Order.Status.DELIVERED -> "Đơn hàng của bạn đã được giao"
            Order.Status.PAID -> "Đơn hàng của bạn đã được thanh toán"
            else -> "Trạng thái đơn hàng đã được cập nhật thành ${newStatus.name}"
        }

        // Send notification to customer
        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            orderId = updatedOrder.id,
            orderStatus = updatedOrder.status,
            message = message
        )

        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            orderId = orderId,
            orderStatus = updatedOrder.status,
            message = message
        )
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

    fun updateOrder(orderId: Int, staffId: Int, request: ConfirmOrderRequest) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { CustomException("Đơn hàng không tồn tại") }

        val staff = staffRepository.findByUserId(staffId)
            ?: throw CustomException("Nhân viên không tồn tại")

        if (order.shop.id != staff.shop.id)
            throw CustomException("Bạn không có quyền cập nhật đơn hàng này")

        if (order.status == Order.Status.COMPLETED || order.status == Order.Status.CANCELED) {
            throw CustomException("Đơn hàng đã hoàn thành hoặc đã hủy")
        }

        if (order.status == Order.Status.DELIVERED) {
            throw CustomException("Đơn hàng đã được giao")
        }

        if (order.status == Order.Status.PENDING) {
            throw CustomException("Đơn hàng đang chờ xác nhận")
        }
        if (order.status == Order.Status.PAID) {
            throw CustomException("Đơn hàng đã được thanh toán")
        }
//        if (order.status == Order.Status.PAID_FAILED) {
//            throw CustomException("Đơn hàng đã thanh toán không thành công")
//        }

        if (request.newPrice == null || request.newPrice < 0) {
            throw CustomException("Giá không hợp lệ")
        }

        val updatedOrder = orderRepository.save(
            order.copy(
                totalPrice = request.newPrice,
                staffResponse = request.staffResponse,
                status = Order.Status.PENDING
            )
        )

        // Send notification to customer
        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            message = "Đơn hàng #${orderId} đã được cập nhật. Giá mới: ${request.newPrice}đ",
            orderId = updatedOrder.id,
            orderStatus = updatedOrder.status,
        )

        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            orderId = orderId,
            orderStatus = Order.Status.PENDING,
            message = request.staffResponse ?: "Đơn hàng đã được cập nhật"
        )
    }

    fun cancelOrder(orderId: Int, staffId: Int, request: CancelOrderRequest) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { CustomException("Đơn hàng không tồn tại") }

        val staff = staffRepository.findByUserId(staffId)
            ?: throw CustomException("Nhân viên không tồn tại")

        if (order.shop.id != staff.shop.id)
            throw CustomException("Bạn không có quyền cập nhật đơn hàng này")

        if (order.status == Order.Status.COMPLETED || order.status == Order.Status.CANCELED) {
            throw CustomException("Đơn hàng đã hoàn thành hoặc đã hủy")
        }

        if (order.status == Order.Status.DELIVERED) {
            throw CustomException("Đơn hàng đã được giao")
        }

        if (order.status == Order.Status.PAID) {
            throw CustomException("Đơn hàng đã được thanh toán")
        }

        val updatedOrder = orderRepository.save(
            order.copy(
                status = Order.Status.CANCELED,
                staffResponse = request.reason
            )
        )

        // Send notification to customer
        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            orderId = updatedOrder.id,
            orderStatus = updatedOrder.status,
            message = "Đơn hàng #${orderId} đã bị hủy: ${request.reason}"
        )

        firebaseNotificationService.sendOrderNotification(
            customerId = updatedOrder.customer.id,
            orderId = orderId,
            orderStatus = Order.Status.CANCELED,
            message = request.reason
        )
    }

    fun getOrdersForStaff(id: Int): List<OrderResponse>? {
        val staff = staffRepository.findByUserId(id)
            ?: throw CustomException("Nhân viên không tồn tại")

        val orders = orderRepository.getOrderForStaff(staff.shop.id)

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


}