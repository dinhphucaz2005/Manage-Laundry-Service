package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String,
    @Column(unique = true, nullable = false)
    val email: String,
    @Column(nullable = false)
    val password: String,
    val phone: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role {
        OWNER, STAFF, CUSTOMER
    }
}

