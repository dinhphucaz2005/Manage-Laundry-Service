package manage.laundry.service.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import manage.laundry.service.entity.ShopService

data class CreateOrderRequest(
    @field:NotNull(message = "ID tiệm không được để trống")
    val shopId: Int,

    val specialInstructions: String? = null,

    @field:NotEmpty(message = "Danh sách dịch vụ không được để trống")
    val items: List<Item>
) {
    fun estimatePrice(services: List<ShopService>): Int {

        var estimatePrice = 0
        items.forEach { item ->
            val serviceItem = services.find { it.id == item.serviceId }
                ?: throw IllegalArgumentException("Dịch vụ không tồn tại")

            if (serviceItem.shop.id != shopId) {
                throw IllegalArgumentException("Dịch vụ không thuộc tiệm này")
            }

            val itemPrice = serviceItem.price * item.quantity
            estimatePrice += itemPrice
        }
        return estimatePrice
    }

    data class Item(
        @field:NotNull(message = "ID dịch vụ không được để trống")
        val serviceId: Int,

        @field:Min(1, message = "Số kg tối thiểu là 1")
        val quantity: Int
    )
}

