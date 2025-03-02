package manage.laundry.service.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateServiceRequest(
    @field:NotBlank(message = "Service name is required")
    val name: String,

    @field:NotBlank(message = "Description is required")
    val description: String,

    @field:Positive(message = "Price must be a positive value")
    val price: BigDecimal,

    val shopId: Int
)
