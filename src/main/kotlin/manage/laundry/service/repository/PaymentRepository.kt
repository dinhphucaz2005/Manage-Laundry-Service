package manage.laundry.service.repository

import manage.laundry.service.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PaymentRepository : JpaRepository<Payment, Int> {

    @Query(
        """
        SELECT SUM(p.amount) 
        FROM Order o
        JOIN Payment p ON o.id = p.order.id
        WHERE o.shop.id = :shopId AND o.status = 'PAID'
    """
    )
    fun getRevenueByShopId(shopId: Int): Long


    @Query("""
        SELECT p.paymentMethod, COUNT(p) 
        FROM Payment p
        JOIN p.order o
        WHERE o.shop.id = :shopId
        GROUP BY p.paymentMethod
    """)
    fun countByPaymentMethod(shopId: Int): List<Array<Any>>
}