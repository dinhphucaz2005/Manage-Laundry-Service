package manage.laundry.service.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AddStaffByEmailRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Position is required")
    val position: String,

    @field:NotBlank(message = "Shop ID is required")
    val shopId: String
)
