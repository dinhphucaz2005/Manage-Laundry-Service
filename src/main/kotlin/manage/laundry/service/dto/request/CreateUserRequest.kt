package manage.laundry.service.dto.request


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import manage.laundry.service.entity.User

data class CreateUserRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "Phone is required")
    val phone: String,

    val role: User.Role
)
