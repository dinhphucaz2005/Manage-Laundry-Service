package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "services")
data class ShopService(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "shop_id")
    val shop: Shop,
    val name: String,
    val description: String,
    val price: Int,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)