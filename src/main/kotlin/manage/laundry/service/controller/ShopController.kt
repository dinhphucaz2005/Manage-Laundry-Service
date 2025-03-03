package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.CreateServiceRequest
import manage.laundry.service.dto.request.ShopRegisterRequest
import manage.laundry.service.dto.request.StaffRegisterRequest
import manage.laundry.service.dto.request.UpdateServiceRequest
import manage.laundry.service.dto.response.RegisterOwnerResponse
import manage.laundry.service.dto.response.RegisterStaffResponse
import manage.laundry.service.dto.response.ShopServiceResponse
import manage.laundry.service.service.ShopService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/owners")
class ShopController(
    private val shopService: ShopService,
) {

    @PostMapping("/register")
    fun registerOwner(
        @RequestBody @Valid request: ShopRegisterRequest
    ): ResponseEntity<ApiResponse<RegisterOwnerResponse>> {
        val result = shopService.registerOwnerWithShop(request)
        return ResponseEntity.ok(ApiResponse.success(result, "Đăng ký chủ tiệm và tạo hồ sơ tiệm thành công"))
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

    @PostMapping("/shops/{shopId}/services")
    fun addService(
        @PathVariable shopId: Int,
        @RequestBody @Valid request: CreateServiceRequest
    ): ResponseEntity<ApiResponse<ShopServiceResponse>> {
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

}
