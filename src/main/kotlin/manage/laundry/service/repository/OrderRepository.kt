package manage.laundry.service.repository

import manage.laundry.service.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Int>
