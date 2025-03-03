package manage.laundry.service.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CustomerRegisterRequest(
    @field:NotBlank(message = "Tên không được để trống")
    val name: String,

    @field:NotBlank(message = "Email không được để trống")
    @field:Email(message = "Email không hợp lệ")
    val email: String,

    @field:NotBlank(message = "Mật khẩu không được để trống")
    @field:Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    val password: String,

    @field:NotBlank(message = "Số điện thoại không được để trống")
    @field:Size(min = 10, max = 11, message = "Số điện thoại phải từ 10 đến 11 số")
    val phone: String
)
