package manage.laundry.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("test")
    fun test(): String = "Hello World"
}
