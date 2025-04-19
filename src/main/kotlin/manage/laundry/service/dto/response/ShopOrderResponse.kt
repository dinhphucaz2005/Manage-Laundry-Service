package manage.laundry.service.dto.response

import manage.laundry.service.entity.Order

import java.time.LocalDateTime

data class ShopOrderResponse(
    val orderId: Int,
    val customerName: String,
    val estimatePrice: Int,
    val totalPrice: Int?,
    val status: Order.Status,
    val createdAt: LocalDateTime
)
