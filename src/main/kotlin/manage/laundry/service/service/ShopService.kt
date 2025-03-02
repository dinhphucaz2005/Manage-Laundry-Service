package manage.laundry.service.service

import jakarta.persistence.EntityNotFoundException
import manage.laundry.service.dto.request.CreateShopRequest
import manage.laundry.service.dto.response.ShopResponse
import manage.laundry.service.dto.response.StaffResponse
import manage.laundry.service.entity.Shop
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.StaffRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalTime


@Service
class ShopService(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val staffRepository: StaffRepository
) {
    fun create(request: CreateShopRequest, ownerId: Int): ShopResponse {
        val owner = userRepository.findById(ownerId)
            .orElseThrow { EntityNotFoundException("Owner not found") }

        val shop = shopRepository.save(
            Shop(
                owner = owner,
                name = request.name,
                location = request.location,
                description = request.description,
                openTime = LocalTime.parse(request.openTime),
                closeTime = LocalTime.parse(request.closeTime)
            )
        )
        return ShopResponse.from(shop)
    }

    fun getStaffs(shopId: Int): List<StaffResponse> {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { EntityNotFoundException("Shop not found") }
        return staffRepository.findByShop(shop)
            .map { StaffResponse.from(it) }
    }

    fun deleteStaff(shopId: Int, staffId: Int): Boolean {
        val shop = shopRepository.findById(shopId)
            .orElseThrow { EntityNotFoundException("Shop not found") }
        val staff = staffRepository.findById(staffId)
            .orElseThrow { EntityNotFoundException("Staff not found") }
        if (staff.shop != shop) {
            throw IllegalArgumentException("Staff does not belong to this shop")
        }
        staffRepository.delete(staff)
        return true
    }
}
