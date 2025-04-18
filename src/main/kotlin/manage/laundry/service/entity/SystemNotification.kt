package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "system_notifications")
data class SystemNotification(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val message: String,

    @Enumerated(EnumType.STRING)
    val severity: manage.laundry.service.dto.system_admin.NotificationSeverity,

    @Column(name = "target_roles")
    val targetRoles: String? = null, // Comma-separated roles

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "expires_at")
    val expiresAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: User
) {
    enum class NotificationSeverity {
        INFO, WARNING, CRITICAL
    }
}

