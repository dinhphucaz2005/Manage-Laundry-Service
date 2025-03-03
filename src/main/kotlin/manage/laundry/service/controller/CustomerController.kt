package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.CreateOrderRequest
import manage.laundry.service.dto.request.CustomerLoginRequest
import manage.laundry.service.dto.request.CustomerRegisterRequest
import manage.laundry.service.dto.request.TrackOrderResponse
import manage.laundry.service.dto.response.CreateOrderResponse
import manage.laundry.service.dto.response.CustomerLoginResponse
import manage.laundry.service.dto.response.RegisterCustomerResponse
import manage.laundry.service.dto.response.ShopSearchResponse
import manage.laundry.service.entity.User
import manage.laundry.service.service.CustomerAuthService
import manage.laundry.service.service.CustomerOrderService
import manage.laundry.service.service.CustomerService
import manage.laundry.service.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val customerAuthService: CustomerAuthService,
    private val customerOrderService: CustomerOrderService,
    private val userService: UserService,
    private val jwtUtil: JwtUtil
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

    @GetMapping("/shops")
    fun searchShops(
        @RequestParam(required = false) location: String?,
        @RequestParam(required = false) service: String?,
        @RequestParam(required = false) minRating: Double?
    ): ResponseEntity<ApiResponse<List<ShopSearchResponse>>> {
        val shops = customerService.searchShops(location, service, minRating)
        return ResponseEntity.ok(
            ApiResponse.success(shops, "Tìm kiếm tiệm giặt thành công")
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: CustomerLoginRequest
    ): ResponseEntity<ApiResponse<CustomerLoginResponse>> {
        val response = customerAuthService.login(request)
        return ResponseEntity.ok(
            ApiResponse.success(response, "Đăng nhập thành công")
        )
    }

    @PostMapping("/orders")
    fun createOrder(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody @Valid request: CreateOrderRequest
    ): ResponseEntity<ApiResponse<CreateOrderResponse>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val userId = jwtUtil.extractUserId(token)

        val user = userService.getUserById(userId)

        if (user.role != User.Role.CUSTOMER) {
            throw Exception("Chỉ khách hàng mới được đặt đơn")
        }

        val response = customerOrderService.createOrder(user.id, request)
        return ResponseEntity.ok(ApiResponse.success(response, "Tạo đơn giặt thành công"))
    }

    @GetMapping("/orders/{orderId}/track")
    fun trackOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<TrackOrderResponse>> {

        val user = userService.authenticateCustomer(authorizationHeader)

        val response = customerOrderService.trackOrder(orderId, user.id)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = "Lấy tiến trình đơn hàng thành công"
            )
        )
    }



}
