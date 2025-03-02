package manage.laundry.service.controller


import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.LoginRequest
import manage.laundry.service.dto.response.AuthResponse
import manage.laundry.service.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.login(request)
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"))
    }
}
