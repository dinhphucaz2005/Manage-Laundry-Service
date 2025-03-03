package manage.laundry.service.service

import manage.laundry.service.common.JwtUtil
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.StaffLoginRequest
import manage.laundry.service.dto.response.StaffLoginResponse
import manage.laundry.service.entity.User
import manage.laundry.service.repository.StaffRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class StaffService(
    private val userRepository: UserRepository,
    private val staffRepository: StaffRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun login(request: StaffLoginRequest): StaffLoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw Exception("Tài khoản không tồn tại")

        if (user.role != User.Role.STAFF) {
            throw Exception("Tài khoản không phải nhân viên")
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw Exception("Mật khẩu không đúng")
        }

        val staff = staffRepository.findByUserId(user.id)
            ?: throw Exception("Nhân viên không thuộc tiệm nào")

        val token = jwtUtil.generateToken(user)

        return StaffLoginResponse(
            token = token,
            staffId = staff.id,
            shopId = staff.shop.id,
            name = user.name
        )
    }
}
