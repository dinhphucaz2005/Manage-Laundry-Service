package manage.laundry.service.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class StaffRegisterRequest(
    @field:NotBlank(message = "Tên không được để trống")
    @field:Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự")
    val name: String,

    @field:NotBlank(message = "Email không được để trống")
    @field:Email(message = "Email không hợp lệ")
    val email: String,

    @field:NotBlank(message = "Mật khẩu không được để trống")
    @field:Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    val password: String,

    @field:NotBlank(message = "Số điện thoại không được để trống")
    @field:Pattern(
        regexp = "^[0-9]{10,11}$",
        message = "Số điện thoại phải có 10 hoặc 11 chữ số"
    )
    val phone: String
)
