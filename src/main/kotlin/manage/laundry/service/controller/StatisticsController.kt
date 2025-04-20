package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.response.RevenueTimeRangeResponse
import manage.laundry.service.dto.response.ServicePopularityResponse
import manage.laundry.service.dto.response.ShopStatisticsResponse
import manage.laundry.service.service.ShopStatisticsService
import manage.laundry.service.service.UserService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/statistics")
class StatisticsController(
    private val shopStatisticsService: ShopStatisticsService,
    private val userService: UserService,
) {

    @GetMapping("/shop/{shopId}")
    fun getShopStatistics(
        @PathVariable shopId: Int,
        @RequestHeader("Authorization") authorizationHeader: String
    ): ApiResponse<ShopStatisticsResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val response = shopStatisticsService.getShopStatistics(shopId, owner.id)
        return ApiResponse.success(data = response)
    }

    @GetMapping("/shop/{shopId}/revenue")
    fun getShopRevenue(
        @PathVariable shopId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime,
    ): ApiResponse<RevenueTimeRangeResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val response = shopStatisticsService.getShopRevenueByTimeRange(shopId, owner.id, startDate, endDate)
        return ApiResponse.success(response)
    }

    @GetMapping("/shop/{shopId}/services/popularity")
    fun getServicePopularity(
        @PathVariable shopId: Int,
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ApiResponse<ServicePopularityResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val response = shopStatisticsService.getServicePopularity(shopId, owner.id)
        return ApiResponse.success(response)
    }
}