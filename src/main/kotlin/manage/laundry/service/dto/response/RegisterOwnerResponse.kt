package manage.laundry.service.dto.response

data class RegisterOwnerResponse(
    val user: UserResponse,
    val shop: ShopResponse
)
