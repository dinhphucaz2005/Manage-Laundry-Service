package manage.laundry.service.controller

import manage.laundry.service.dto.request.CreateUserRequest
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.response.UserResponse
import manage.laundry.service.service.UserService
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody @Valid request: CreateUserRequest): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.createUser(request)
        return ResponseEntity.ok(ApiResponse.success(UserResponse.fromEntity(user), "User created successfully"))
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(ApiResponse.success(UserResponse.fromEntity(user), "User fetched successfully"))
    }
}
