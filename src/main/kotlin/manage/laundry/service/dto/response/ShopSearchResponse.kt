package manage.laundry.service.dto.response


data class ShopSearchResponse(
    val shopId: Int,
    val name: String,
    val location: String,
    val description: String,
    val openTime: String,
    val closeTime: String,
    val averageRating: Double
)
