package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateServiceRequest(
    @field:NotBlank(message = "Tên dịch vụ không được để trống")
    val name: String,

    @field:NotBlank(message = "Mô tả không được để trống")
    val description: String,

    @field:NotNull(message = "Giá dịch vụ không được để trống")
    @field:Positive(message = "Giá dịch vụ phải lớn hơn 0")
    val price: BigDecimal
)
