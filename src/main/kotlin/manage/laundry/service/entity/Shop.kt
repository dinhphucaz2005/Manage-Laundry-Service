package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "shops")
data class Shop(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "owner_id")
    val owner: User,
    val name: String,
    val location: String,
    val description: String,
    @Column(name = "open_time")
    val openTime: LocalTime,
    @Column(name = "close_time")
    val closeTime: LocalTime,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
