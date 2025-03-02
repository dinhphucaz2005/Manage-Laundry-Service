package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "staff_requests")
data class StaffRequest(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    val shop: Shop,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Status {
        PENDING, ACCEPTED, REJECTED
    }
}
