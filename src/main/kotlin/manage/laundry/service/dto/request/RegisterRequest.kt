package manage.laundry.service.dto.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String
)
