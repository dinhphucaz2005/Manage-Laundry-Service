package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne @JoinColumn(name = "order_id")
    val order: Order,
    val amount: Int,
    @Column(name = "payment_method")
    val paymentMethod: String,
    @Column(name = "paid_at")
    val paidAt: LocalDateTime = LocalDateTime.now()
)