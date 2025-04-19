package manage.laundry.service.dto.response



data class ShopServiceResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val shopId: Int
)

data class ShopDetailServiceResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
)