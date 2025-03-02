package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.LoginRequest
import manage.laundry.service.dto.request.RegisterRequest
import manage.laundry.service.dto.response.LoginResponse
import manage.laundry.service.dto.response.ProfileResponse
import manage.laundry.service.entity.User
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.StaffRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val shopRepository: ShopRepository,
    private val staffRepository: StaffRepository,
    private val jwtUtil: JwtUtil,
) {

    fun registerUser(request: RegisterRequest, role: User.Role): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phone = request.phone,
            role = role,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return userRepository.save(user)
    }


    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid email or password")

        if (passwordEncoder.encode(request.password) != user.password) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val token = jwtUtil.generateToken(user.id, user.email, user.role)

        return LoginResponse(
            token = token,
            userId = user.id,
            email = user.email,
            role = user.role
        )
    }

    fun getProfile(authHeader: String): ProfileResponse {
        val userId = jwtUtil.extractUserId(authHeader)
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        return ProfileResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            phone = user.phone,
            role = user.role
        )
    }

}
