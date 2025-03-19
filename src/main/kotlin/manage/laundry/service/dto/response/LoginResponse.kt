package manage.laundry.service.dto.response

data class LoginResponse(
    val token: String,
    val id: Int,
    val name: String,
    val email: String,
    val shop: ShopResponse
)