package manage.laundry.service.controller

import manage.laundry.service.dto.system_admin.*
import manage.laundry.service.service.SystemAdminService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/admin")
class SystemAdminController(private val systemAdminService: SystemAdminService) {

    @PutMapping("/owners/{ownerId}/verify")
    fun verifyShopOwner(
        @PathVariable ownerId: Int,
        @RequestBody verificationRequest: OwnerVerificationRequest
    ): ResponseEntity<OwnerVerificationResponse> {
        val response = systemAdminService.verifyShopOwner(ownerId, verificationRequest)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/system/monitoring")
    fun getSystemMonitoring(): ResponseEntity<SystemMonitoringResponse> {
        val response = systemAdminService.getSystemMonitoring()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/system/security")
    fun updateSecuritySettings(
        @RequestBody securityUpdateRequest: SecurityUpdateRequest,
        @RequestAttribute("userId") adminId: Int // This would be set by authentication filter
    ): ResponseEntity<SecurityUpdateResponse> {
        val response = systemAdminService.updateSecuritySettings(securityUpdateRequest, adminId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/system/statistics")
    fun getSystemStatistics(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?
    ): ResponseEntity<SystemStatisticsResponse> {
        val response = systemAdminService.getSystemStatistics(startDate, endDate)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/system/notifications")
    fun sendSystemNotification(
        @RequestBody notificationRequest: SystemNotificationRequest,
        @RequestAttribute("userId") adminId: Int // This would be set by authentication filter
    ): ResponseEntity<NotificationResponse> {
        val response = systemAdminService.sendSystemNotification(notificationRequest, adminId)
        return ResponseEntity.ok(response)
    }
}