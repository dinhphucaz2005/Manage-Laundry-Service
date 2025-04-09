package manage.laundry.service.repository

import manage.laundry.service.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SystemAdminRepository : JpaRepository<User, Int> {
    @Query("SELECT u FROM User u WHERE u.role = 'OWNER' AND u.id = :ownerId")
    fun findOwnerById(ownerId: Int): User?
    
    @Query("SELECT COUNT(u) FROM User u")
    fun countTotalUsers(): Int
    
    @Query("SELECT COUNT(s) FROM Shop s")
    fun countTotalShops(): Int
    
    @Query("SELECT COUNT(o) FROM Order o")
    fun countTotalOrders(): Int
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED'")
    fun countCompletedOrders(): Int
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'CANCELLED'")
    fun countCancelledOrders(): Int
    
    @Query("SELECT AVG(o.totalPrice) FROM Order o WHERE o.status = 'COMPLETED'")
    fun getAverageOrderValue(): Double
}