package manage.laundry.service.dto.response

data class PopularServiceStats(
    val serviceId: Int,
    val serviceName: String,
    val orderCount: Long,
    val totalQuantity: Long,
    val totalRevenue: Long
)