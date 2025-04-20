package manage.laundry.service.dto.response

data class ServicePopularity(
    val serviceId: Int,
    val serviceName: String,
    val totalUsage: Long,
    val totalRevenue: Long,
    val orderCount: Long
)