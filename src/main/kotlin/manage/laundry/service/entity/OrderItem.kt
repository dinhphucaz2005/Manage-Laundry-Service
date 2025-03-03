package manage.laundry.service.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "order_id")
    val order: Order,
    @ManyToOne @JoinColumn(name = "service_id")
    val shopService: ShopService,
    val quantity: Int,
    val price: BigDecimal
)