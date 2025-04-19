package manage.laundry.service.dto.response

import manage.laundry.service.entity.Order

import java.time.LocalDateTime

data class OrderResponse(
    val id: Int,
    val shopName: String,
    val customerName: String? = null,
    val estimatePrice: Int,
    val totalPrice: Int?,
    val status: Order.Status,
    val specialInstructions: String?,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime,
    val items: List<OrderItemResponse>,
) {
    data class OrderItemResponse(
        val id: Int,
        val name: String,
        val quantity: Int,
        val price: Int,
        val totalPrice: Int
    )
}
