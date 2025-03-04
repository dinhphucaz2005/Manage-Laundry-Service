package manage.laundry.service.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderHistoryResponse(
    val orderId: Int,
    val shopName: String,
    val totalPrice: BigDecimal,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
