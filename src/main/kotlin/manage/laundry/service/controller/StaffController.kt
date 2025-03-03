package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.StaffLoginRequest
import manage.laundry.service.dto.response.StaffLoginResponse
import manage.laundry.service.service.StaffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/staff")
class StaffController(
    private val staffService: StaffService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: StaffLoginRequest
    ): ResponseEntity<ApiResponse<StaffLoginResponse>> {
        val response = staffService.login(request)
        return ResponseEntity.ok(
            ApiResponse.success(response, "Đăng nhập thành công")
        )
    }
}
