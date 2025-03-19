package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.*
import manage.laundry.service.dto.response.*
import manage.laundry.service.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/owners")
class ShopController(
    private val shopService: ShopService,
    private val userService: UserService,
    private val ownerAuthService: OwnerAuthService,
    private val customerOrderService: CustomerOrderService,
) {

    @PostMapping("/register")
    fun registerOwner(
        @RequestBody @Valid request: ShopRegisterRequest
    ): ResponseEntity<ApiResponse<RegisterOwnerResponse>> {
        val result = shopService.registerOwnerWithShop(request)
        return ResponseEntity.ok(ApiResponse.success(result, "Đăng ký chủ tiệm và tạo hồ sơ tiệm thành công"))
    }

    @GetMapping("/shops/{shopId}/staffs")
    fun getStaffs(
        @PathVariable shopId: Int
    ): ResponseEntity<ApiResponse<GetStaffResponse>> {
        val response = shopService.getStaffs(shopId)
        return ResponseEntity.ok(ApiResponse.success(response, "Lấy danh sách nhân viên thành công"))
    }


    @PostMapping("/shops/{shopId}/staffs")
    fun addStaff(
        @PathVariable shopId: Int,
        @RequestBody @Valid request: StaffRegisterRequest
    ): ResponseEntity<ApiResponse<RegisterStaffResponse>> {
        val response = shopService.addStaffToShop(shopId, request)
        return ResponseEntity.ok(
            ApiResponse.success(response, "Thêm nhân viên vào tiệm thành công")
        )
    }

    @GetMapping("/shops/{shopId}/services")
    fun getService(@PathVariable shopId: Int): ResponseEntity<ApiResponse<List<ShopServiceResponse>>> {
        val response = shopService.getServices(shopId)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Lấy danh sách dịch vụ thành công",
                data = response
            )
        )
    }


    @PostMapping("/shops/{shopId}/services")
    fun addService(
        @PathVariable shopId: Int,
        @RequestBody @Valid request: CreateServiceRequest
    ): ResponseEntity<ApiResponse<List<ShopServiceResponse>>> {
        val response = shopService.addServiceToShop(shopId, request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Thêm dịch vụ thành công",
                data = response
            )
        )
    }

    @PutMapping("/services/{serviceId}")
    fun updateService(
        @PathVariable serviceId: Int,
        @RequestBody @Valid request: UpdateServiceRequest
    ): ResponseEntity<ApiResponse<String>> {
        shopService.updateService(serviceId, request)
        return ResponseEntity.ok(
            ApiResponse.success(message = "Cập nhật dịch vụ thành công")
        )
    }

    @DeleteMapping("/services/{serviceId}")
    fun deleteService(
        @PathVariable serviceId: Int
    ): ResponseEntity<ApiResponse<String>> {
        shopService.deleteService(serviceId)
        return ResponseEntity.ok(
            ApiResponse.success(message = "Xóa dịch vụ thành công")
        )
    }

    @GetMapping("/shops/{shopId}/orders")
    fun getShopOrders(
        @PathVariable shopId: Int,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<ApiResponse<List<ShopOrderResponse>>> {

        val owner = userService.authenticateOwner(authorizationHeader)

        val response = shopService.getShopOrders(shopId, owner.id)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = "Lấy danh sách đơn hàng của tiệm thành công"
            )
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: OwnerLoginRequest
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = ownerAuthService.ownerLogin(request)
        return ResponseEntity.ok(
            ApiResponse.success(
                data = response,
                message = "Đăng nhập thành công"
            )
        )
    }

    @PutMapping("/orders/{orderId}")
    fun updateOrder(
        @PathVariable orderId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody @Valid request: UpdateOrderRequest
    ): ResponseEntity<ApiResponse<String>> {

        val user = userService.authenticateOwner(authorizationHeader)
        customerOrderService.updateOrderByOwner(orderId, request, user.id)

        return ResponseEntity.ok(
            ApiResponse.success(message = "Cập nhật đơn hàng thành công")
        )
    }
}
