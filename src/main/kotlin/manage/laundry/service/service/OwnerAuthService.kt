package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.OwnerLoginRequest
import manage.laundry.service.dto.response.LoginResponse
import manage.laundry.service.dto.response.ShopResponse
import manage.laundry.service.entity.User
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class OwnerAuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val shopRepository: ShopRepository
) {

    fun ownerLogin(request: OwnerLoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw CustomException("Email không tồn tại")

        if (user.role != User.Role.OWNER) {
            throw CustomException("Tài khoản không phải chủ tiệm")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw CustomException("Mật khẩu không chính xác")
        }

        val shop = shopRepository.getShopsByOwnerId(user.id).firstOrNull()
            ?: throw CustomException("Chủ tiệm chưa tạo tiệm")

        val token = jwtUtil.generateToken(user)

        return LoginResponse(
            token = token,
            id = user.id,
            name = user.name,
            email = user.email,
            shop = ShopResponse.fromEntity(shop)
        )
    }
}