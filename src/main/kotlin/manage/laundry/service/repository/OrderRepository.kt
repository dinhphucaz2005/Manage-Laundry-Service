package manage.laundry.service.repository

import manage.laundry.service.entity.Order
import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Int> {

    fun findByShop(shop: Shop): MutableList<Order>

}
