package manage.laundry.service.dto.request

data class UpdateShopRequest(
    val name: String?,
    val location: String?,
    val description: String?,
    val openTime: String?,
    val closeTime: String?
)
