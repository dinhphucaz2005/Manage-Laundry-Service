package manage.laundry.service.service

import manage.laundry.service.dto.system_admin.*
import manage.laundry.service.entity.*
import manage.laundry.service.repository.SecuritySettingsRepository
import manage.laundry.service.repository.SystemAdminRepository
import manage.laundry.service.repository.SystemNotificationRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class SystemAdminService(
    private val systemAdminRepository: SystemAdminRepository,
    private val userRepository: UserRepository,
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val systemNotificationRepository: SystemNotificationRepository
) {
    fun verifyShopOwner(ownerId: Int, request: OwnerVerificationRequest): OwnerVerificationResponse {
        val owner = systemAdminRepository.findOwnerById(ownerId)
            ?: throw RuntimeException("Owner with ID $ownerId not found")

        // In a real implementation, you would update the owner's verification status
        // For simplicity, we're just returning a response

        return OwnerVerificationResponse(
            message = "Owner verified successfully",
            ownerId = ownerId,
            verified = request.verified
        )
    }

    fun getSystemMonitoring(): SystemMonitoringResponse {
        // In a real implementation, you would gather system metrics
        return SystemMonitoringResponse(
            activeUsers = 120,
            cpuUsage = 45.5,
            memoryUsage = 62.3,
            diskUsage = 58.7,
            serviceStatus = mapOf(
                "api-service" to "RUNNING",
                "notification-service" to "RUNNING",
                "payment-service" to "RUNNING"
            ),
            lastUpdated = LocalDateTime.now().toString()
        )
    }

    fun updateSecuritySettings(request: SecurityUpdateRequest, adminId: Int): SecurityUpdateResponse {
        val currentSettings = securitySettingsRepository.findById(1)
            .orElse(SecuritySettings())

        val updatedSettings = SecuritySettings(
            passwordMinLength = request.passwordPolicy?.minLength ?: currentSettings.passwordMinLength,
            passwordRequireUppercase = request.passwordPolicy?.requireUppercase
                ?: currentSettings.passwordRequireUppercase,
            passwordRequireSpecial = request.passwordPolicy?.requireSpecialChar
                ?: currentSettings.passwordRequireSpecial,
            passwordRequireNumber = request.passwordPolicy?.requireNumber ?: currentSettings.passwordRequireNumber,
            sessionTimeoutMinutes = request.sessionTimeout ?: currentSettings.sessionTimeoutMinutes,
            twoFactorRequired = request.twoFactorRequired ?: currentSettings.twoFactorRequired,
            updatedAt = LocalDateTime.now(),
            updatedByUserId = adminId
        )

        securitySettingsRepository.save(updatedSettings)

        return SecurityUpdateResponse(
            message = "Security settings updated successfully",
            updatedAt = updatedSettings.updatedAt.toString()
        )
    }

    fun getSystemStatistics(startDate: LocalDate?, endDate: LocalDate?): SystemStatisticsResponse {
        val start = startDate ?: LocalDate.now().minusMonths(1)
        val end = endDate ?: LocalDate.now()

        // In a real implementation, you would query the database with date range filters
        // For this example, we'll return some sample data

        return SystemStatisticsResponse(
            totalUsers = systemAdminRepository.countTotalUsers(),
            totalShops = systemAdminRepository.countTotalShops(),
            totalOrders = systemAdminRepository.countTotalOrders(),
            ordersCompleted = systemAdminRepository.countCompletedOrders(),
            ordersCancelled = systemAdminRepository.countCancelledOrders(),
            averageOrderValue = systemAdminRepository.getAverageOrderValue(),
            revenueByDate = generateSampleRevenueData(start, end),
            period = "${start.format(DateTimeFormatter.ISO_DATE)} to ${end.format(DateTimeFormatter.ISO_DATE)}"
        )
    }

    private fun generateSampleRevenueData(start: LocalDate, end: LocalDate): Map<LocalDate, Double> {
        val result = mutableMapOf<LocalDate, Double>()
        var current = start

        while (!current.isAfter(end)) {
            result[current] = (1000..5000).random().toDouble()
            current = current.plusDays(1)
        }

        return result
    }

    fun sendSystemNotification(request: SystemNotificationRequest, adminId: Int): NotificationResponse {
        val admin = userRepository.findById(adminId)
            .orElseThrow { RuntimeException("Admin with ID $adminId not found") }

        val expiresAt = request.expiresAt?.let { LocalDateTime.parse(it) }

        val notification = SystemNotification(
            title = request.title,
            message = request.message,
            severity = NotificationSeverity.valueOf(request.severity.name),
            targetRoles = request.targetRoles?.joinToString(","),
            expiresAt = expiresAt,
            createdBy = admin
        )

        systemNotificationRepository.save(notification)

        // Calculate number of recipients (in a real app, this would be done differently)
        val recipientCount = if (request.targetRoles == null) {
            userRepository.count().toInt()
        } else {
            userRepository.countByRoleIn(request.targetRoles.map { User.Role.valueOf(it) })
        }

        return NotificationResponse(
            message = "System notification sent successfully",
            sentTo = recipientCount,
            notificationId = notification.id.toString()
        )
    }
}