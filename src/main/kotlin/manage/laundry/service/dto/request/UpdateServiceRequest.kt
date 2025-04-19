package manage.laundry.service.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateServiceRequest(
    @field:NotBlank(message = "Tên dịch vụ không được để trống")
    @field:Size(min = 2, max = 100, message = "Tên dịch vụ phải từ 2 đến 100 ký tự")
    val name: String,

    @field:NotBlank(message = "Mô tả không được để trống")
    val description: String,

    @field:Min(value = 1000, message = "Giá dịch vụ phải lớn hơn 1000")
    val price: Int
)
