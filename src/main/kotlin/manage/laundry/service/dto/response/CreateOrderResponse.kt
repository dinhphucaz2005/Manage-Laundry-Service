package manage.laundry.service.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateOrderResponse(
    val orderId: Int,
    val totalPrice: BigDecimal,
    val status: String,
    val createdAt: LocalDateTime
)
