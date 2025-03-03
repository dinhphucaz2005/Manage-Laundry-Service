package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.OwnerLoginRequest
import manage.laundry.service.dto.response.LoginResponse
import manage.laundry.service.entity.User
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class OwnerAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun ownerLogin(request: OwnerLoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw Exception("Email không tồn tại")

        if (user.role != User.Role.OWNER) {
            throw Exception("Tài khoản không phải chủ tiệm")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw Exception("Mật khẩu không chính xác")
        }

        val token = jwtUtil.generateToken(user)

        return LoginResponse(
            token = token,
            id = user.id,
            name = user.name,
            email = user.email
        )
    }
}