package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "security_settings")
data class SecuritySettings(
    @Id
    val id: Int = 1, // Only one record for system-wide settings

    @Column(name = "password_min_length")
    val passwordMinLength: Int = 8,

    @Column(name = "password_require_uppercase")
    val passwordRequireUppercase: Boolean = true,

    @Column(name = "password_require_special")
    val passwordRequireSpecial: Boolean = true,

    @Column(name = "password_require_number")
    val passwordRequireNumber: Boolean = true,

    @Column(name = "session_timeout_minutes")
    val sessionTimeoutMinutes: Int = 30,

    @Column(name = "two_factor_required")
    val twoFactorRequired: Boolean = false,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val updatedByUserId: Int? = null
)