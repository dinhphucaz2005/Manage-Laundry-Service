package manage.laundry.service.repository

import manage.laundry.service.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderItemRepository : JpaRepository<OrderItem, Int> {


    @Query("SELECT e FROM OrderItem e WHERE e.order.id = :orderId")
    fun findAllByOrderIdIn(orderId: Int): List<OrderItem>

    @Query(
        """
        SELECT s.id, s.name, COUNT(oi), SUM(oi.quantity), SUM(oi.price)
        FROM OrderItem oi
        JOIN oi.shopService s
        JOIN oi.order o
        WHERE o.shop.id = :shopId
        GROUP BY s.id, s.name
        ORDER BY COUNT(oi) DESC
    """
    )
    fun getPopularServicesByShopId(shopId: Int): List<Array<Any>>

    @Query(
        """
    SELECT 
        s.id,
        s.name,
        SUM(oi.quantity),
        SUM(oi.price),
        COUNT(oi.id)
    FROM OrderItem oi
    JOIN oi.order o
    JOIN oi.shopService s
    WHERE o.shop.id = :shopId
      AND o.status = 'PAID'
    GROUP BY s.id, s.name
    ORDER BY SUM(oi.quantity) DESC
"""
    )
    fun getServiceStatsByShopId(shopId: Int): List<Array<Any>>

}