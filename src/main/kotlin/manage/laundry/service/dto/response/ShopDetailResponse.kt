package manage.laundry.service.dto.response

import manage.laundry.service.entity.Shop
import manage.laundry.service.entity.ShopService

data class ShopDetailResponse(
    val id: Int,
    val name: String,
    val location: String,
    val openTime: String,
    val closeTime: String,
    val services: List<ShopDetailServiceResponse> = emptyList(),
) {
    companion object {
        fun mapper(shop: Shop, services: List<ShopService>): ShopDetailResponse {
            return ShopDetailResponse(
                id = shop.id,
                name = shop.name,
                location = shop.location,
                openTime = shop.openTime,
                closeTime = shop.closeTime,
                services = services.map {
                    ShopDetailServiceResponse(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        price = it.price,
                    )
                }
            )
        }
    }
}
