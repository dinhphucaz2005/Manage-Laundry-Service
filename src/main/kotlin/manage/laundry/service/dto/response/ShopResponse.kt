package manage.laundry.service.dto.response

import manage.laundry.service.entity.Shop

data class ShopResponse(
    val id: Int,
    val name: String,
    val location: String,
    val description: String,
    val openTime: String,
    val closeTime: String,
    val ownerId: Int
) {
    companion object {
        fun from(shop: Shop) = ShopResponse(
            id = shop.id,
            name = shop.name,
            location = shop.location,
            description = shop.description,
            openTime = shop.openTime.toString(),
            closeTime = shop.closeTime.toString(),
            ownerId = shop.owner.id
        )
    }
}
