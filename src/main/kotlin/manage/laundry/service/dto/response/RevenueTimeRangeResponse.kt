package manage.laundry.service.dto.response

data class RevenueTimeRangeResponse(
    val totalOrders: Int,
    val totalRevenue: Int,
    val averageOrderValue: Int,
    val revenueByDay: Map<java.time.LocalDate, Int>
)