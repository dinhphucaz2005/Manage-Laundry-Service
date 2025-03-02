package manage.laundry.service.dto.response

data class AuthResponse(
    val token: String,
    val user: UserResponse
)
