package manage.laundry.service.dto.response


import java.time.LocalDateTime

data class CreateOrderResponse(
    val orderId: Int,
    val estimatePrice: Int,
    val status: String,
    val createdAt: LocalDateTime
)
