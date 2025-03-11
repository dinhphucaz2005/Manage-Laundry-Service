package manage.laundry.service.dto.response

data class RegisterStaffResponse(
    val staffs: List<UserResponse>,
    val shop: ShopResponse
)
