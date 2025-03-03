package manage.laundry.service.dto.response

data class StaffLoginResponse(
    val token: String,
    val staffId: Int,
    val shopId: Int,
    val name: String
)
