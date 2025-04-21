package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import manage.laundry.service.dto.request.SystemAdminSendNotificationRequest
import manage.laundry.service.entity.Order
import manage.laundry.service.service.FirebaseNotificationService
import manage.laundry.service.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.random.Random

@RestController
class HelloController(
    private val firebaseNotificationService: FirebaseNotificationService,
    private val userService: UserService
) {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<ApiResponse<String>> {

        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }

    @GetMapping("/test-firebase")
    fun testFirebase(): ResponseEntity<ApiResponse<Boolean>> {
        val result = firebaseNotificationService.testDatabaseConnection()
        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        return ResponseEntity.ok(ApiResponse.success(result, "Request received at $time"))
    }

    @GetMapping("/test-firebase-notification/{id}")
    fun testFirebaseNotification(
        @PathVariable("id") id: Int
    ): ResponseEntity<ApiResponse<String>> {
        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        firebaseNotificationService.sendOrderNotification(
            customerId = id,
            message = "Your order is pending.",
            orderId = Random.nextInt(),
            orderStatus = Order.Status.DELIVERED,
        )

        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }

    @PostMapping("/send-notification")
    fun sendNotification(
        @RequestBody request: SystemAdminSendNotificationRequest,
    ): ResponseEntity<ApiResponse<String>> {
        request.validate()
        firebaseNotificationService.sendSystemNotification(message = request.message)
        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"
        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }
}
