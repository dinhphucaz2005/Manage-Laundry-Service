package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.entity.User
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) {

    fun getUserById(userId: Int): User {
        return userRepository.findById(userId)
            .orElseThrow { CustomException("Người dùng không tồn tại") }
    }

    fun authenticateCustomer(authorizationHeader: String): User {
        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val userId = jwtUtil.extractUserId(token)
        val user = getUserById(userId)

        if (user.role != User.Role.CUSTOMER) {
            throw CustomException("Chỉ khách hàng mới được thực hiện hành động này")
        }

        return user
    }

    fun authenticateOwner(authorizationHeader: String): User {
        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val userId = jwtUtil.extractUserId(token)
        val user = getUserById(userId)

        if (user.role != User.Role.OWNER) {
            throw CustomException("Chỉ chủ tiệm mới được thực hiện hành động này")
        }

        return user
    }

}
