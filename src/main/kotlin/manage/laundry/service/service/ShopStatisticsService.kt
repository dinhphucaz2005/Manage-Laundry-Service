package manage.laundry.service.service

import manage.laundry.service.dto.response.*
import manage.laundry.service.entity.Order
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ShopStatisticsService @Autowired constructor(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val orderItemRepository: OrderItemRepository,
    private val shopRepository: ShopRepository,
) {

    fun getShopStatistics(shopId: Int, ownerId: Int): ShopStatisticsResponse {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { CustomException("Tiệm không tồn tại") }

        if (shop.owner.id != ownerId) {
            throw CustomException("Bạn không có quyền xem thống kê của tiệm này")
        }

        return ShopStatisticsResponse(
            totalOrders = orderRepository.countByShopId(shopId),
            ordersByStatus = mapResultToOrderByStatus(orderRepository.countOrderByStatus()).associate { it.status to it.orderCount },
            totalRevenue = paymentRepository.getRevenueByShopId(shopId),
            paymentMethods = paymentRepository.countByPaymentMethod(shopId).associate {
                it[0] as String to (it[1] as Long)
            },
            popularServices = orderItemRepository.getPopularServicesByShopId(shopId).map {
                PopularServiceStats(
                    serviceId = it[0] as Int,
                    serviceName = it[1] as String,
                    orderCount = it[2] as Long,
                    totalQuantity = it[3] as Long,
                    totalRevenue = it[4] as Long,
                )
            }
        )
    }

    fun getShopRevenueByTimeRange(
        shopId: Int,
        ownerId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): RevenueTimeRangeResponse {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { CustomException("Tiệm không tồn tại") }

        if (shop.owner.id != ownerId) {
            throw CustomException("Bạn không có quyền xem doanh thu của tiệm này")
        }

        val allOrders = orderRepository.findByShop(shop)
            .filter { it.status == Order.Status.PAID }
            .filter { it.createdAt.isAfter(startDate) && it.createdAt.isBefore(endDate) }

        // Group by day for the date range
        val revenueByDay = allOrders.groupBy { it.createdAt.toLocalDate() }
            .mapValues { (_, orders) -> orders.sumOf { it.totalPrice ?: 0 } }

        // Count orders in time range
        val totalOrders = allOrders.size
        val totalRevenue = allOrders.sumOf { it.totalPrice ?: 0 }
        val averageOrderValue = if (totalOrders > 0) totalRevenue / totalOrders else 0

        return RevenueTimeRangeResponse(
            totalOrders = totalOrders,
            totalRevenue = totalRevenue,
            averageOrderValue = averageOrderValue,
            revenueByDay = revenueByDay
        )
    }

    fun getServicePopularity(shopId: Int, ownerId: Int): ServicePopularityResponse {
        return ServicePopularityResponse(
            orderItemRepository.getServiceStatsByShopId(shopId)
                .map {
                    ServicePopularity(
                        serviceId = it[0] as Int,
                        serviceName = it[1] as String,
                        totalUsage = it[2] as Long,
                        totalRevenue = it[3] as Long,
                        orderCount = it[4] as Long,
                    )
                }
        )
    }
}