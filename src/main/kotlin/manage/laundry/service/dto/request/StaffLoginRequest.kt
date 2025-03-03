package manage.laundry.service.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class StaffLoginRequest(
    @field:NotBlank(message = "Email không được để trống")
    @field:Email(message = "Email không hợp lệ")
    val email: String,

    @field:NotBlank(message = "Mật khẩu không được để trống")
    val password: String
)
