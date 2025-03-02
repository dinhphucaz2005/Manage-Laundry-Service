package manage.laundry.service.dto.response


import manage.laundry.service.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val role: User.Role,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            phone = user.phone,
            role = user.role,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
