package manage.laundry.service.dto.response

data class CustomerLoginResponse(
    val token: String,
    val name: String,
    val email: String,
    val phone: String,
    val userId: Int
)
