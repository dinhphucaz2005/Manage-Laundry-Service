package manage.laundry.service.service

import manage.laundry.service.entity.Order
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Service
class FirebaseNotificationService(
    @Value("\${firebase.database.url}") private val databaseUrl: String,
) {
    private val webClient = WebClient.create()

    fun sendOrderNotification(customerId: Int, orderId: Int, orderStatus: Order.Status, message: String) {

        val path = "notifications/${customerId}.json"
        val url = buildUrl(path)

        webClient.put()
            .uri(url)
            .bodyValue("""
                {
                    "orderId": $orderId,
                    "message": "$message",
                    "status": "${orderStatus.name}",
                    "timestamp": ${System.currentTimeMillis()}
                }
            """.trimIndent())
            .retrieve()
            .bodyToMono(String::class.java)
            .subscribe(
                { println("✅ Notification sent: $it") },
                { error -> println("❌ Failed to send notification: ${error.message}") }
            )
    }

    fun testDatabaseConnection(): Boolean {
        val path = "test_connection.json"
        val url = buildUrl(path)
        val testValue = "Connection test: ${LocalDateTime.now()}"

        return try {
            webClient.put()
                .uri(url)
                .bodyValue("{\"testValue\": \"$testValue\"}")
                .retrieve()
                .bodyToMono(String::class.java)
                .doOnNext { println("✅ Firebase test connection OK: $it") }
                .doOnError { e -> println("❌ Firebase test connection FAILED: ${e.message}") }
                .block() != null
        } catch (e: Exception) {
            println("❌ Firebase test connection FAILED: ${e.message}")
            false
        }
    }

    private fun buildUrl(path: String): String {
        val base = if (databaseUrl.endsWith("/")) databaseUrl else "$databaseUrl/"
        return "$base$path"
    }
}
