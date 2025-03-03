package manage.laundry.service.dto.response

data class RegisterStaffResponse(
    val staff: UserResponse,
    val shop: ShopResponse
)
