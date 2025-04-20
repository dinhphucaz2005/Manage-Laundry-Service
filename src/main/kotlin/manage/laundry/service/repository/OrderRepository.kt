package manage.laundry.service.repository

import manage.laundry.service.entity.Order
import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

data class OrderByStatus(
    val status: Order.Status,
    val orderCount: Long
)

fun mapResultToOrderByStatus(result: List<Array<Any>>): List<OrderByStatus> {
    return result.map { row ->
        val statusString = row[0] as? Order.Status ?: throw IllegalArgumentException("Có lỗi xảy ra khi lấy trạng thái đơn hàng")
        val orderCount = row[1] as? Long ?: throw IllegalArgumentException("Có lỗi xảy ra khi lấy số lượng đơn hàng")

        OrderByStatus(
            status = statusString,
            orderCount = orderCount
        )
    }
}



interface OrderRepository : JpaRepository<Order, Int> {

    fun findByShop(shop: Shop): MutableList<Order>


    @Query("""
    SELECT o.status AS status, COUNT(o) AS order_count
    FROM Order o
    GROUP BY o.status
    ORDER BY order_count DESC
""")
    fun countOrderByStatus(): List<Array<Any>>



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


    @Query(
        """
        SELECT o FROM Order o 
        WHERE o.shop.id = :shopId AND o.status = :status 
        ORDER BY o.createdAt DESC
    """
    )
    fun findAllByStatusAndShopId(status: Order.Status, shopId: Int): List<Order>

    @Query(
        """
        SELECT o FROM Order o
        WHERE o.shop.id = :shopId 
        AND o.status IN (:includedStatuses)
        ORDER BY o.createdAt DESC
    """
    )
    fun getOrderForStaff(
        shopId: Int, includedStatuses: List<Order.Status> = listOf(
            Order.Status.DELIVERED,
            Order.Status.PAID,
//            Order.Status.PAID_FAILED,
        )
    ): List<Order>

    @Query(
        """
        select count(o) from Order o
        where o.shop.id = :shopId
    """
    )
    fun countByShopId(shopId: Int): Long

    @Query("""
        select sum(o.totalPrice) from Order o
        where o.shop.id = :shopId
    """)
    fun getRevenueByShopId(shopId: Int): Long


}
