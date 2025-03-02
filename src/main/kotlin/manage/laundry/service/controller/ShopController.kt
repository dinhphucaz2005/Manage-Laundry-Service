package manage.laundry.service.controller

import jakarta.validation.Valid
import manage.laundry.service.common.ApiResponse
import manage.laundry.service.common.JwtUtil
import manage.laundry.service.dto.request.CreateShopRequest
import manage.laundry.service.dto.response.ShopResponse
import manage.laundry.service.service.ShopService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/shops")
class ShopController(
    private val shopService: ShopService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping
    fun createShop(
        @Valid @RequestBody request: CreateShopRequest,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ApiResponse<ShopResponse>> {
        val ownerId = jwtUtil.extractUserId(token)
        val shop = shopService.create(request, ownerId)
        return ResponseEntity.ok(ApiResponse.success(shop, "Shop created successfully"))
    }
}
