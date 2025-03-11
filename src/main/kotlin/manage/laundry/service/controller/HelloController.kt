package manage.laundry.service.controller

import manage.laundry.service.common.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<ApiResponse<String>> {

        val now = LocalDateTime.now()
        val time = "${now.hour}:${now.minute}:${now.second} - ${now.dayOfMonth}/${now.monthValue}/${now.year}"

        return ResponseEntity.ok(ApiResponse.success("Request received at $time"))
    }
}
