package manage.laundry.service

import manage.laundry.service.repository.OrderRepository
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.UserRepository
import manage.laundry.service.entity.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.LocalTime

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        shopRepository: ShopRepository,
        orderRepository: OrderRepository
    ) = CommandLineRunner {
        return@CommandLineRunner
        if (userRepository.count() == 0L) {
            // Tạo user
            val user = User(
                name = "PHUC",
                email = "example@gmail.com",
                password = "phuc!@1705",
                phone = "0000000000",
                role = User.Role.OWNER
            )
            val savedUser = userRepository.save(user)

            // Tạo vài shop
            val shop1 = Shop(
                owner = savedUser,
                name = "Phuc Laundry 1",
                location = "123 Le Loi, Hue",
                description = "Dịch vụ giặt ủi chất lượng cao",
                openTime = LocalTime.of(8, 0),
                closeTime = LocalTime.of(20, 0)
            )
            val shop2 = Shop(
                owner = savedUser,
                name = "Phuc Laundry 2",
                location = "456 Tran Hung Dao, Da Nang",
                description = "Giặt sấy siêu tốc",
                openTime = LocalTime.of(7, 30),
                closeTime = LocalTime.of(21, 0)
            )

            val savedShop1 = shopRepository.save(shop1)
            val savedShop2 = shopRepository.save(shop2)

            // Tạo vài order cho shop1
            val order1 = Order(
                customer = savedUser,
                shop = savedShop1,
                totalPrice = BigDecimal(100_000),
                status = Order.Status.PENDING,
                specialInstructions = "Giặt riêng đồ trắng"
            )
            val order2 = Order(
                customer = savedUser,
                shop = savedShop1,
                totalPrice = BigDecimal(150_000),
                status = Order.Status.PROCESSING,
                specialInstructions = "Ủi thẳng đồ vest"
            )
            orderRepository.saveAll(listOf(order1, order2))

            val order3 = Order(
                customer = savedUser,
                shop = savedShop2,
                totalPrice = BigDecimal(300_000),
                status = Order.Status.PROCESSING,
            )

            val order4 = Order(
                customer = savedUser,
                shop = savedShop2,
                totalPrice = BigDecimal(40_000),
                status = Order.Status.PROCESSING,
            )

            orderRepository.saveAll(listOf(order3, order4))

            println("✅ Dữ liệu mẫu đã được tạo xong!")
        } else {
            println("⚠️ Dữ liệu đã tồn tại, không tạo mới.")
        }
    }
}
