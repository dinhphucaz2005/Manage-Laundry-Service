package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.LoginRequest
import manage.laundry.service.dto.response.AuthResponse
import manage.laundry.service.dto.response.UserResponse
import manage.laundry.service.exception.NotFoundException
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository
) {
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw NotFoundException("User not found")

        if (user.password != request.password) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = JwtUtil.generateToken(user.id, user.email, user.role.name)

        return AuthResponse(
            token = token,
            user = UserResponse.fromEntity(user)
        )
    }
}
