package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.CustomerLoginRequest
import manage.laundry.service.dto.response.CustomerLoginResponse
import manage.laundry.service.entity.User
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CustomerAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun login(request: CustomerLoginRequest): CustomerLoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw CustomException("Email không tồn tại")

        if (user.role != User.Role.CUSTOMER) {
            throw CustomException("Tài khoản không phải khách hàng")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw CustomException("Mật khẩu không đúng")
        }

        val token = jwtUtil.generateToken(user)

        return CustomerLoginResponse(
            token = token,
            name = user.name,
            email = user.email,
            phone = user.phone
        )
    }
}
