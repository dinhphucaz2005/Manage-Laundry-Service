package manage.laundry.service.repository

import manage.laundry.service.entity.Order
import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Int> {

    fun findByShop(shop: Shop): MutableList<Order>


    @Query(
        """
        SELECT o FROM Order o
        JOIN Staff s ON o.shop.id = s.shop.id
        WHERE s.user.id = :staffId
        AND o.status NOT IN (:excludedStatuses)
    """
    )
    fun findAllActiveOrdersByStaffId(
        staffId: Int,
        excludedStatuses: List<Order.Status>
    ): List<Order>

    @Query(
        """
    SELECT o FROM Order o
    WHERE o.customer.id = :customerId
    ORDER BY o.createdAt DESC
    """
    )
    fun findAllByCustomerId(customerId: Int): List<Order>

}
