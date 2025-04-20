package manage.laundry.service.dto.response

import manage.laundry.service.entity.Order

data class ShopStatisticsResponse(
    val totalOrders: Long,
    val ordersByStatus: Map<Order.Status, Long>,
    val totalRevenue: Long,
    val paymentMethods: Map<String, Long>,
    val popularServices: List<PopularServiceStats>
)