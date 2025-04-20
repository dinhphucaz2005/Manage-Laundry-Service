package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import manage.laundry.service.entity.Order
import manage.laundry.service.service.FirebaseNotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class HelloController(
    private val firebaseNotificationService: FirebaseNotificationService
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

    @GetMapping("/test-firebase-notification")
    fun testFirebaseNotification(): ResponseEntity<ApiResponse<String>> {
        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        firebaseNotificationService.sendOrderNotification(
            customerId = 1,
            message = "Your order is pending.",
            orderId = 0,
            orderStatus = Order.Status.DELIVERED,
        )

        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }

    @GetMapping("/test-firebase-order-status")
    fun testFirebaseOrderStatus(): ResponseEntity<ApiResponse<String>> {
        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        firebaseNotificationService.sendOrderNotification(
            customerId = 1,
            message = "Your order is pending.",
            orderId = 0,
            orderStatus = Order.Status.DELIVERED,
        )

        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }
}
