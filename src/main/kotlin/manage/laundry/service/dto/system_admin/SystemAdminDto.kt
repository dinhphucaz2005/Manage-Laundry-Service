package manage.laundry.service.dto.system_admin

import java.time.LocalDate

data class OwnerVerificationRequest(
    val verified: Boolean,
    val notes: String? = null
)

data class OwnerVerificationResponse(
    val message: String,
    val ownerId: Int,
    val verified: Boolean
)

data class SecurityUpdateRequest(
    val passwordPolicy: PasswordPolicy? = null,
    val sessionTimeout: Int? = null,
    val twoFactorRequired: Boolean? = null
)

data class PasswordPolicy(
    val minLength: Int,
    val requireUppercase: Boolean,
    val requireSpecialChar: Boolean,
    val requireNumber: Boolean
)

data class SecurityUpdateResponse(
    val message: String,
    val updatedAt: String
)

data class SystemMonitoringResponse(
    val activeUsers: Int,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val diskUsage: Double,
    val serviceStatus: Map<String, String>,
    val lastUpdated: String
)

data class SystemStatisticsResponse(
    val totalUsers: Int,
    val totalShops: Int,
    val totalOrders: Int,
    val ordersCompleted: Int,
    val ordersCancelled: Int,
    val averageOrderValue: Double,
    val revenueByDate: Map<LocalDate, Double>,
    val period: String
)

data class SystemNotificationRequest(
    val title: String,
    val message: String,
    val severity: NotificationSeverity,
    val targetRoles: List<String>? = null, // If null, send to all users
    val expiresAt: String? = null
)

enum class NotificationSeverity {
    INFO, WARNING, CRITICAL
}

data class NotificationResponse(
    val message: String,
    val sentTo: Int, // Number of recipients
    val notificationId: String
)