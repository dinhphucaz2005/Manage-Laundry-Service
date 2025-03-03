package manage.laundry.service.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateOrderRequest(
    @field:NotNull(message = "ID tiệm không được để trống")
    val shopId: Int,

    val specialInstructions: String? = null,

    @field:NotEmpty(message = "Danh sách dịch vụ không được để trống")
    val items: List<Item>
) {
    data class Item(
        @field:NotNull(message = "ID dịch vụ không được để trống")
        val serviceId: Int,

        @field:Min(1, message = "Số kg tối thiểu là 1")
        val quantity: Int
    )
}

