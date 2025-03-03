package manage.laundry.service.dto.response

import manage.laundry.service.entity.Shop

data class ShopResponse(
    val id: Int,
    val name: String,
    val location: String,
    val openTime: String,
    val closeTime: String
) {
    companion object {
        fun fromEntity(shop: Shop) = ShopResponse(
            id = shop.id,
            name = shop.name,
            location = shop.location,
            openTime = shop.openTime,
            closeTime = shop.closeTime
        )
    }
}
