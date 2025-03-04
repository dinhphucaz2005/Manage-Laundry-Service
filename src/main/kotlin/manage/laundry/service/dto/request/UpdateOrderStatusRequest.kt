package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotBlank

data class UpdateOrderStatusRequest(
    @field:NotBlank(message = "Trạng thái không được để trống")
    val status: String
)
