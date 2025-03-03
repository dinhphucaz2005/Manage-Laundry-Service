package manage.laundry.service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

interface PasswordEncoder {
    fun encode(password: String): String = password
    fun matches(requestPassword: String, hashedPassword: String): Boolean {
        return hashedPassword == requestPassword
    }
}

class NoOpPasswordEncoder : PasswordEncoder {
    companion object {
        fun getInstance(): PasswordEncoder {
            return NoOpPasswordEncoder()
        }
    }
}

@Configuration
@Profile("dev")
class DevPasswordConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}
