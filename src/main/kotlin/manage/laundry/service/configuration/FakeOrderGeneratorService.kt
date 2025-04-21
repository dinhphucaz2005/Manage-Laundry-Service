package manage.laundry.service.configuration

import manage.laundry.service.entity.*
import manage.laundry.service.repository.*
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Service as SpringService

@SpringService
@EnableScheduling
class FakeOrderGeneratorService(
    private val shopRepository: ShopRepository,
    private val customerRepository: CustomerRepository,
    private val shopServiceRepository: ShopServiceRepository,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val userRepository: UserRepository,
    private val paymentRepository: PaymentRepository,
) {

    private val random = Random()
    private val specialInstructions = listOf(
        "Hãy giặt cẩn thận áo trắng",
        "Sử dụng nước xả vải hương lavender",
        "Đừng sử dụng chất tẩy mạnh",
        "Cần giao trước 5 giờ chiều",
        "Giặt riêng quần áo màu đậm",
        null
    )

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    @Transactional
    fun generateFakeOrders() {
        return//Uncomment this line to enable the order generation
        val shop = shopRepository.findById(2).orElseThrow()
        val customers = customerRepository.findAll().toList().mapNotNull { customer ->
            userRepository.findById(customer.user.id).orElseThrow()
        }
        val services = shopServiceRepository.findByShop(shop)

        if (customers.isEmpty() || services.isEmpty()) {
            return
        }

        // Create 200 random orders
        val orderCount = 20000

        for (i in 1..orderCount) {
            createRandomOrder(shop, customers, services)
        }
    }

    fun randomLocalDateTime(): LocalDateTime {
        // random LocalDateTime between 2025-01-01 and 2025-04-20
        val start = LocalDateTime.of(2025, 1, 1, 0, 0)
        val end = LocalDateTime.now()
        val startEpoch = start.toEpochSecond(ZoneOffset.UTC)
        val endEpoch = end.toEpochSecond(ZoneOffset.UTC)
        val randomEpoch = startEpoch + (random.nextLong() % (endEpoch - startEpoch))
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC)
    }

    fun randomLocalDateTimeAfter(start: LocalDateTime): LocalDateTime {
        // random LocalDateTime after the given start time
        val end = LocalDateTime.now(ZoneOffset.UTC)
        val startEpoch = start.toEpochSecond(ZoneOffset.UTC)
        val endEpoch = end.toEpochSecond(ZoneOffset.UTC)
        val randomEpoch = startEpoch + (random.nextLong() % (endEpoch - startEpoch))
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC)
    }

    private fun createRandomOrder(shop: Shop, customers: List<User>, services: List<ShopService>) {

        // Create new order
        val createAt = randomLocalDateTime()
        val updateAt = randomLocalDateTimeAfter(createAt)

        val order = Order(
            customer = customers.random(),
            shop = shop,
            status = Order.Status.entries.random(),
            specialInstructions = specialInstructions[random.nextInt(specialInstructions.size)],
            staffResponse = null,
            estimatePrice = 0,
            createdAt = createAt,
            updatedAt = updateAt,
        )

        val savedOrder = orderRepository.save(order)

        // Add 1-3 random services as order items
        val itemCount = random.nextInt(10) + 1
        val selectedServices = services.shuffled().take(itemCount)
        var totalPrice = 0

        for (service in selectedServices) {
            val quantity = random.nextInt(3) + 1
            val price = service.price * quantity
            totalPrice += price

            val orderItem = OrderItem(
                order = savedOrder,
                shopService = service,
                quantity = quantity,
                price = price
            )

            orderItemRepository.save(orderItem)
        }

        // Update order with total price
        orderRepository.save(
            savedOrder.copy(
                estimatePrice = totalPrice,
                totalPrice = totalPrice + random.nextInt(5) * 10_000
            )
        )
        if (savedOrder.status == Order.Status.PAID) {
            val payment = Payment(
                order = savedOrder,
                amount = savedOrder.totalPrice!!,
                paymentMethod = listOf("CASH", "BANK").random(),
                paidAt = savedOrder.updatedAt,
            )
            paymentRepository.save(payment)
        }
    }
}