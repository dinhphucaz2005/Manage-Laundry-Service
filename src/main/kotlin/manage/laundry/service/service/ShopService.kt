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
import manage.laundry.service.exception.CustomException
import manage.laundry.service.repository.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class ShopService(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val staffRepository: StaffRepository,
    private val shopServiceRepository: ShopServiceRepository,
    private val passwordEncoder: PasswordEncoder,
    private val orderRepository: OrderRepository
) {

    fun registerOwnerWithShop(request: ShopRegisterRequest): RegisterOwnerResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw CustomException("Email đã được sử dụng")
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

    fun getStaffs(shopId: Int): GetStaffResponse {
        val shop = shopRepository.findById(shopId).orElseThrow {
            CustomException("Không tìm thấy tiệm")
        }

        val staffs = staffRepository.findByShop(shop)
            .map { staff -> UserResponse.fromEntity(staff.user) }

        return GetStaffResponse(staffs = staffs)
    }


    fun addStaffToShop(shopId: Int, request: StaffRegisterRequest): RegisterStaffResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw CustomException("Email đã được sử dụng")
        }

        val shop = shopRepository.findById(shopId).orElseThrow {
            CustomException("Không tìm thấy tiệm")
        }

        val staffUser = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phone = request.phone,
            role = User.Role.STAFF
        )
        val savedStaffUser = userRepository.save(staffUser)

        staffRepository.save(
            Staff(
                user = savedStaffUser,
                shop = shop
            )
        )

        val staffs = staffRepository.findByShop(shop)
            .map { staff -> UserResponse.fromEntity(staff.user) }

        return RegisterStaffResponse(
            staffs = staffs,
            shop = ShopResponse.fromEntity(shop)
        )
    }

    fun getServices(shopId: Int): List<ShopServiceResponse> {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { CustomException("Không tìm thấy tiệm với id = $shopId") }

        val services = shopServiceRepository.findByShop(shop)
            .map { savedService ->
                ShopServiceResponse(
                    id = savedService.id,
                    name = savedService.name,
                    description = savedService.description,
                    price = savedService.price,
                    shopId = shop.id
                )
            }

        return services
    }


    fun addServiceToShop(shopId: Int, request: CreateServiceRequest): List<ShopServiceResponse> {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { CustomException("Không tìm thấy tiệm với id = $shopId") }

        val service = ShopService(
            shop = shop,
            name = request.name,
            description = request.description,
            price = request.price
        )
        shopServiceRepository.save(service)

        val services = shopServiceRepository.findByShop(shop)
            .map { savedService ->
                ShopServiceResponse(
                    id = savedService.id,
                    name = savedService.name,
                    description = savedService.description,
                    price = savedService.price,
                    shopId = shop.id
                )
            }

        return services
    }


    @Transactional
    fun updateService(serviceId: Int, request: UpdateServiceRequest) {
        val service = shopServiceRepository.findById(serviceId)
            .orElseThrow { CustomException("Không tìm thấy dịch vụ với ID: $serviceId") }

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
            .orElseThrow { CustomException("Dịch vụ không tồn tại") }
        shopServiceRepository.delete(service)
    }

    fun getShopOrders(shopId: Int, ownerId: Int): List<ShopOrderResponse> {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { CustomException("Tiệm không tồn tại") }

        if (shop.owner.id != ownerId) {
            throw CustomException("Bạn không có quyền xem đơn hàng của tiệm này")
        }

        val orders = orderRepository.findByShop(shop)

        return orders.map { order ->
            ShopOrderResponse(
                orderId = order.id,
                customerName = order.customer.name,
                totalPrice = order.totalPrice,
                status = order.status,
                createdAt = order.createdAt
            )
        }
    }


}
