package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "staffs")
data class Staff(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne @JoinColumn(name = "shop_id")
    val shop: Shop,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
