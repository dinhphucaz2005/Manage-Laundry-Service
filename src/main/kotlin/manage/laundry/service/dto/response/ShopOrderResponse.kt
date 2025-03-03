package manage.laundry.service.dto.response

import manage.laundry.service.entity.Order
import java.math.BigDecimal
import java.time.LocalDateTime

data class ShopOrderResponse(
    val orderId: Int,
    val customerName: String,
    val totalPrice: BigDecimal,
    val status: Order.Status,
    val createdAt: LocalDateTime
)
