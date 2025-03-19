package manage.laundry.service.repository

import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ShopRepository : JpaRepository<Shop, Int> {
    @Query(
        """
        SELECT s FROM Shop s
        WHERE (:location IS NULL OR s.location LIKE %:location%)
        AND (:minRating IS NULL OR s.averageRating >= :minRating)
        AND (:service IS NULL OR EXISTS (
            SELECT ss FROM ShopService ss WHERE ss.shop = s AND ss.name LIKE %:service%
        ))
    """
    )
    fun searchShops(
        @Param("location") location: String?,
        @Param("service") service: String?,
        @Param("minRating") minRating: Double?
    ): List<Shop>


    fun getShopsByOwnerId(ownerId: Int): List<Shop>

}
