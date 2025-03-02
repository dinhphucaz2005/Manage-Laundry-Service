package manage.laundry.service.dto.response

import manage.laundry.service.entity.User

data class LoginResponse(
    val token: String,
    val userId: Int,
    val email: String,
    val role: User.Role
)
