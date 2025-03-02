package manage.laundry.service.repository

import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository

interface ShopRepository : JpaRepository<Shop, Int>
