package manage.laundry.service.dto.response

import manage.laundry.service.entity.User

data class ProfileResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val role: User.Role
)
