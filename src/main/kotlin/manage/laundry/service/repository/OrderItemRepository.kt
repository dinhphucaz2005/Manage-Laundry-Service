package manage.laundry.service.repository

import manage.laundry.service.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderItemRepository : JpaRepository<OrderItem, Int> {


    @Query("SELECT e FROM OrderItem e WHERE e.order.id = :orderId")
    fun findAllByOrderIdIn(orderId: Int): List<OrderItem>
}