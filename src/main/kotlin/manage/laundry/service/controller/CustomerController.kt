package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.CustomerRegisterRequest
import manage.laundry.service.dto.response.RegisterCustomerResponse
import manage.laundry.service.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping("/register")
    fun registerCustomer(
        @RequestBody @Valid request: CustomerRegisterRequest
    ): ResponseEntity<ApiResponse<RegisterCustomerResponse>> {
        val response = customerService.registerCustomer(request)
        return ResponseEntity.ok(
            ApiResponse.success(response, "Đăng ký tài khoản khách hàng thành công")
        )
    }
}
