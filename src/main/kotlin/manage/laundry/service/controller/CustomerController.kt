package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.CreateOrderRequest
import manage.laundry.service.dto.request.CustomerLoginRequest
import manage.laundry.service.dto.request.CustomerRegisterRequest
import manage.laundry.service.dto.response.*
import manage.laundry.service.entity.User
import manage.laundry.service.exception.CustomException
import manage.laundry.service.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val customerAuthService: CustomerAuthService,
    private val customerOrderService: CustomerOrderService,
    private val orderService: OrderService,
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
            throw CustomException("Chỉ khách hàng mới được đặt đơn")
        }

        val response = customerOrderService.createOrder(user.id, request)
        return ResponseEntity.ok(ApiResponse.success(response, "Tạo đơn giặt thành công"))
    }

    @GetMapping("/orders/{orderId}/track")
    fun trackOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<OrderResponse>> {

        val user = userService.authenticateCustomer(authorizationHeader)

        val response = customerOrderService.trackOrder(orderId, user.id)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = "Lấy tiến trình đơn hàng thành công"
            )
        )
    }

    @GetMapping("/orders")
    fun getOrderHistory(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<List<OrderResponse>>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val role = jwtUtil.extractUserRole(token)

        if (role != User.Role.CUSTOMER) {
            throw CustomException("Chỉ khách hàng mới có quyền xem lịch sử đơn hàng")
        }

        val customerId = jwtUtil.extractUserId(token)

        val history = orderService.getOrderHistory(customerId)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = history,
                message = "Lấy lịch sử đơn hàng thành công"
            )
        )
    }

    @PutMapping("/orders/{orderId}/confirm")
    fun confirmOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<Nothing>> {

        val user = userService.authenticateCustomer(authorizationHeader)

        customerOrderService.confirmOrder(orderId, user.id)

        return ResponseEntity.ok(ApiResponse.success(message = "Xác nhận đơn hàng thành công"))
    }

    @PutMapping("/orders/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<Nothing>> {

        val user = userService.authenticateCustomer(authorizationHeader)

        customerOrderService.cancelOrder(orderId, user.id)

        return ResponseEntity.ok(ApiResponse.success(message = "Hủy đơn hàng thành công"))
    }
}
