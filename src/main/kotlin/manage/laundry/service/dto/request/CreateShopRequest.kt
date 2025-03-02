package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotBlank


data class CreateShopRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Location is required")
    val location: String,

    val description: String,

    @field:NotBlank(message = "Open time is required (HH:mm)")
    val openTime: String,

    @field:NotBlank(message = "Close time is required (HH:mm)")
    val closeTime: String
)
