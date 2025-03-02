package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "reviews")
data class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @OneToOne @JoinColumn(name = "order_id")
    val order: Order,
    @ManyToOne @JoinColumn(name = "customer_id")
    val customer: User,
    val rating: Int,
    val comment: String? = null,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
