package manage.laundry.service.repository

import manage.laundry.service.entity.Shop
import manage.laundry.service.entity.Staff
import manage.laundry.service.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface StaffRepository : JpaRepository<Staff, Int> {

    fun findByUserAndShop(user: User, shop: Shop): Staff?

    fun findByShop(shop: Shop?): List<Staff>

    fun findByUserId(userId: Int): Staff?

}