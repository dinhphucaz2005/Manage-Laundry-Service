package manage.laundry.service.repository

import manage.laundry.service.entity.Shop
import manage.laundry.service.entity.ShopService
import org.springframework.data.jpa.repository.JpaRepository

interface ShopServiceRepository : JpaRepository<ShopService, Int> {

    fun findByShop(shop: Shop): List<ShopService>

}
