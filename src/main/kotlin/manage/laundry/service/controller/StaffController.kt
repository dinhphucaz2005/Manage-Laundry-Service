package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.CancelOrderRequest
import manage.laundry.service.dto.request.ConfirmOrderRequest
import manage.laundry.service.dto.request.StaffLoginRequest
import manage.laundry.service.dto.request.UpdateOrderStatusRequest
import manage.laundry.service.dto.response.OrderResponse
import manage.laundry.service.dto.response.StaffLoginResponse
import manage.laundry.service.entity.User
import manage.laundry.service.exception.CustomException
import manage.laundry.service.service.OrderService
import manage.laundry.service.service.StaffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/staff")
class StaffController(
    private val staffService: StaffService,
    private val orderService: OrderService,
    private val jwtUtil: JwtUtil,
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


    @GetMapping("/orders")
    fun getActiveOrders(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<ApiResponse<List<OrderResponse>>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()

        val role = jwtUtil.extractUserRole(token)

        if (role != User.Role.STAFF) {
            throw CustomException("Chỉ nhân viên mới được xem danh sách đơn hàng")
        }

        val id = jwtUtil.extractUserId(token)

        val orders = orderService.getActiveOrdersAssignedToStaff(id)

        return ResponseEntity.ok(
            ApiResponse.success(
                data = orders,
                message = "Lấy danh sách đơn hàng thành công"
            )
        )
    }

    @PutMapping("/orders/{orderId}/status")
    fun updateOrderStatus(
        @PathVariable orderId: Int,
        @RequestBody @Valid request: UpdateOrderStatusRequest,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<ApiResponse<String>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val role = jwtUtil.extractUserRole(token)

        if (role != User.Role.STAFF) {
            throw CustomException("Chỉ nhân viên mới có quyền cập nhật trạng thái đơn hàng")
        }

        val staffId = jwtUtil.extractUserId(token)

        orderService.updateOrderStatus(orderId, request.status, staffId)

        return ResponseEntity.ok(
            ApiResponse.success(message = "Cập nhật trạng thái đơn hàng thành công")
        )
    }

    @PutMapping("/orders/{orderId}/confirm")
    fun confirmOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody request: ConfirmOrderRequest,
    ): ResponseEntity<ApiResponse<String>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val role = jwtUtil.extractUserRole(token)

        if (role != User.Role.STAFF) {
            throw CustomException("Chỉ nhân viên mới có quyền xác nhận đơn hàng")
        }

        val staffId = jwtUtil.extractUserId(token)

        orderService.updateOrder(orderId, staffId, request)

        return ResponseEntity.ok(ApiResponse.success(message = "Xác nhận đơn hàng thành công"))
    }

    @PutMapping("/orders/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody request: CancelOrderRequest,
    ): ResponseEntity<ApiResponse<String>> {

        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val role = jwtUtil.extractUserRole(token)

        if (role != User.Role.STAFF) {
            throw CustomException("Chỉ nhân viên mới có quyền hủy đơn hàng")
        }

        val staffId = jwtUtil.extractUserId(token)

        orderService.cancelOrder(orderId, staffId, request)

        return ResponseEntity.ok(ApiResponse.success(message = "Hủy đơn hàng thành công"))
    }


}
