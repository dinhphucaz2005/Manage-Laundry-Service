package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotNull
import manage.laundry.service.entity.Order

data class UpdateOrderRequest(
    @field:NotNull(message = "Trạng thái không được để trống")
    val status: Order.Status,

    val specialInstructions: String? = null
)
