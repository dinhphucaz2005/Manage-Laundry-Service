package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateOrderStatusRequest(
    @field:NotBlank(message = "Trạng thái không được để trống")
    val status: String
)

data class ConfirmOrderRequest(
    val newPrice: Int?,
    val staffResponse: String?,
)

data class CancelOrderRequest(
    val reason: String,
)