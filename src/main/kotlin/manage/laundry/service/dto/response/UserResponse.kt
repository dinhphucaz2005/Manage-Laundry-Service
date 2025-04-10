package manage.laundry.service.dto.response

import manage.laundry.service.entity.User


data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val role: String
) {
    companion object {
        fun fromEntity(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            phone = user.phone,
            role = user.role.name
        )
    }
}
