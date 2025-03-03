package manage.laundry.service.service

import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.CustomerRegisterRequest
import manage.laundry.service.dto.response.RegisterCustomerResponse
import manage.laundry.service.dto.response.ShopSearchResponse
import manage.laundry.service.entity.Customer
import manage.laundry.service.entity.User
import manage.laundry.service.repository.CustomerRepository
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val shopRepository: ShopRepository
) {

    fun registerCustomer(request: CustomerRegisterRequest): RegisterCustomerResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email đã được sử dụng")
        }

        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phone = request.phone,
            role = User.Role.CUSTOMER
        )
        val savedUser = userRepository.save(user)

        val customer = Customer(user = savedUser)
        val savedCustomer = customerRepository.save(customer)

        return RegisterCustomerResponse(
            customerId = savedCustomer.id,
            userId = savedUser.id,
            name = savedUser.name,
            email = savedUser.email,
            phone = savedUser.phone
        )
    }

    fun searchShops(location: String?, service: String?, minRating: Double?): List<ShopSearchResponse> {
        val shops = shopRepository.searchShops(location, service, minRating)
        return shops.map {
            ShopSearchResponse(
                shopId = it.id,
                name = it.name,
                location = it.location,
                description = it.description ?: "No description",
                openTime = it.openTime,
                closeTime = it.closeTime,
                averageRating = it.averageRating
            )
        }
    }
}
