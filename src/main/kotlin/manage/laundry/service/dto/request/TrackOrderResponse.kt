package manage.laundry.service.dto.request

import manage.laundry.service.entity.Order
import java.math.BigDecimal
import java.time.LocalDateTime

data class TrackOrderResponse(
    val orderId: Int,
    val shopName: String,
    val status: Order.Status,
    val totalPrice: BigDecimal,
    val specialInstructions: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
