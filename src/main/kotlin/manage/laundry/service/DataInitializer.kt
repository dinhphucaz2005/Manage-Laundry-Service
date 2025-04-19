package manage.laundry.service

import manage.laundry.service.repository.OrderRepository
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        shopRepository: ShopRepository,
        orderRepository: OrderRepository
    ) = CommandLineRunner {
    }
}
