package manage.laundry.service.dto.response

import manage.laundry.service.entity.Order
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderResponse(
    val id: Int,
    val shopName: String,
    val customerName: String,
    val totalPrice: BigDecimal,
    val status: Order.Status,
    val specialInstructions: String?,
    val createdAt: LocalDateTime
)
