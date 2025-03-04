package manage.laundry.service.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "customer_id")
    val customer: User,
    @ManyToOne @JoinColumn(name = "shop_id")
    val shop: Shop,
    @Column(name = "total_price")
    val totalPrice: BigDecimal,
    @Enumerated(EnumType.STRING)
    val status: Status = Status.PENDING,
    @Column(name = "special_instructions")
    val specialInstructions: String? = null,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Status {
        PENDING,
        CONFIRMED,
        PROCESSING,
        IN_PROGRESS,
        READY_FOR_DELIVERY,
        COMPLETED,
        CANCELLED
    }
}
