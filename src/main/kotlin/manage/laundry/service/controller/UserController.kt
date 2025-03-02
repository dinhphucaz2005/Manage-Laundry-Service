package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.LoginRequest
import manage.laundry.service.dto.request.RegisterRequest
import manage.laundry.service.dto.response.LoginResponse
import manage.laundry.service.entity.User
import manage.laundry.service.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register/owner")
    fun registerOwner(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<User>> {
        val user = userService.registerUser(request, User.Role.OWNER)
        return ResponseEntity.ok(ApiResponse.success(user, "Owner registered successfully"))
    }

    @PostMapping("/register/staff")
    fun registerStaff(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<User>> {
        val user = userService.registerUser(request, User.Role.STAFF)
        return ResponseEntity.ok(ApiResponse.success(user, "Staff registered successfully"))
    }

    @PostMapping("/register/customer")
    fun registerCustomer(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<User>> {
        val user = userService.registerUser(request, User.Role.CUSTOMER)
        return ResponseEntity.ok(ApiResponse.success(user, "Customer registered successfully"))
    }


    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = userService.login(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"))
    }

}
