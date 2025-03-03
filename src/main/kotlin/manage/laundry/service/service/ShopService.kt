package manage.laundry.service.service

import jakarta.transaction.Transactional
import manage.laundry.service.configuration.PasswordEncoder
import manage.laundry.service.dto.request.CreateServiceRequest
import manage.laundry.service.dto.request.ShopRegisterRequest
import manage.laundry.service.dto.request.StaffRegisterRequest
import manage.laundry.service.dto.request.UpdateServiceRequest
import manage.laundry.service.dto.response.*
import manage.laundry.service.entity.Shop
import manage.laundry.service.entity.ShopService
import manage.laundry.service.entity.Staff
import manage.laundry.service.entity.User
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.ShopServiceRepository
import manage.laundry.service.repository.StaffRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime


@Service
class ShopService(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val staffRepository: StaffRepository,
    private val shopServiceRepository: ShopServiceRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerOwnerWithShop(request: ShopRegisterRequest): RegisterOwnerResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng")
        }

        val owner = User(
            name = request.ownerName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phone = request.phone,
            role = User.Role.OWNER
        )
        val savedOwner = userRepository.save(owner)

        val shop = Shop(
            owner = savedOwner,
            name = request.shopName,
            location = request.address,
            openTime = request.openTime,
            closeTime = request.closeTime
        )
        val savedShop = shopRepository.save(shop)

        return RegisterOwnerResponse(
            user = UserResponse.fromEntity(savedOwner),
            shop = ShopResponse.fromEntity(savedShop)
        )
    }

    fun addStaffToShop(shopId: Int, request: StaffRegisterRequest): RegisterStaffResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã được sử dụng")
        }

        val shop = shopRepository.findById(shopId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tiệm")
        }

        val staffUser = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phone = request.phone,
            role = User.Role.STAFF
        )
        val savedStaffUser = userRepository.save(staffUser)

        val staff = Staff(
            user = savedStaffUser,
            shop = shop
        )
        staffRepository.save(staff)

        return RegisterStaffResponse(
            staff = UserResponse.fromEntity(savedStaffUser),
            shop = ShopResponse.fromEntity(shop)
        )
    }

    fun addServiceToShop(shopId: Int, request: CreateServiceRequest): ShopServiceResponse {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { Exception("Không tìm thấy tiệm với id = $shopId") }

        val service = ShopService(
            shop = shop,
            name = request.name,
            description = request.description,
            price = request.price
        )
        val savedService = shopServiceRepository.save(service)

        return ShopServiceResponse(
            id = savedService.id,
            name = savedService.name,
            description = savedService.description,
            price = savedService.price,
            shopId = shop.id
        )
    }


    @Transactional
    fun updateService(serviceId: Int, request: UpdateServiceRequest) {
        val service = shopServiceRepository.findById(serviceId)
            .orElseThrow { Exception("Không tìm thấy dịch vụ với ID: $serviceId") }

        val updatedService = service.copy(
            name = request.name,
            description = request.description,
            price = request.price,
            updatedAt = LocalDateTime.now()
        )

        shopServiceRepository.save(updatedService)
    }

    fun deleteService(serviceId: Int) {
        val service = shopServiceRepository.findById(serviceId)
            .orElseThrow { Exception("Dịch vụ không tồn tại") }
        shopServiceRepository.delete(service)
    }

}
