package manage.laundry.service.dto.response

import java.math.BigDecimal

data class ShopServiceResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val shopId: Int
)
