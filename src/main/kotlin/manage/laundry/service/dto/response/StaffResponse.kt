package manage.laundry.service.dto.response

data class StaffResponse(
    val staffId: Int,
    val userId: Int,
    val name: String,
    val email: String,
    val phone: String,
    val position: String,
    val shopId: Int
)
