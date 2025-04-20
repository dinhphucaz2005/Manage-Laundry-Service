package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.response.RevenueTimeRangeResponse
import manage.laundry.service.dto.response.ServicePopularityResponse
import manage.laundry.service.dto.response.ShopStatisticsResponse
import manage.laundry.service.repository.ShopRepository
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
    private val shopRepository: ShopRepository,
) {

    @GetMapping("/shop")
    fun getShopStatistics(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ApiResponse<ShopStatisticsResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val shop = shopRepository.getShopsByOwnerId(owner.id)
            .firstOrNull()
            ?: throw IllegalArgumentException("Shop not found for the given owner ID")
        val response = shopStatisticsService.getShopStatistics(shopId = shop.id, ownerId = owner.id)
        return ApiResponse.success(data = response)
    }

    @GetMapping("/shop/revenue")
    fun getShopRevenue(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime,
    ): ApiResponse<RevenueTimeRangeResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val shop = shopRepository.getShopsByOwnerId(owner.id)
            .firstOrNull()
            ?: throw IllegalArgumentException("Shop not found for the given owner ID")

        val response = shopStatisticsService.getShopRevenueByTimeRange(
            shopId = shop.id,
            ownerId = owner.id,
            startDate = startDate,
            endDate = endDate
        )
        return ApiResponse.success(response)
    }

    @GetMapping("/shop/services/popularity")
    fun getServicePopularity(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ApiResponse<ServicePopularityResponse> {
        val owner = userService.authenticateOwner(authorizationHeader = authorizationHeader)
        val shop = shopRepository.getShopsByOwnerId(owner.id)
            .firstOrNull()
            ?: throw IllegalArgumentException("Shop not found for the given owner ID")
        val response = shopStatisticsService.getServicePopularity(shopId = shop.id, ownerId = owner.id)
        return ApiResponse.success(response)
    }
}