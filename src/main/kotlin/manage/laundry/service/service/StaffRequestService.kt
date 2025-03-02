package manage.laundry.service.service

import manage.laundry.service.entity.Staff
import manage.laundry.service.entity.StaffRequest
import manage.laundry.service.repository.ShopRepository
import manage.laundry.service.repository.StaffRepository
import manage.laundry.service.repository.StaffRequestRepository
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StaffRequestService(
    private val staffRequestRepository: StaffRequestRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository,
    private val staffRepository: StaffRepository
) {
    fun createStaffRequest(userId: Int, shopId: Int): StaffRequest {
        val user = userRepository.findById(userId).orElseThrow { throw Exception("User not found") }
        val shop = shopRepository.findById(shopId).orElseThrow { throw Exception("Shop not found") }

        val staffRequest = StaffRequest(
            user = user,
            shop = shop,
            status = StaffRequest.Status.PENDING
        )
        return staffRequestRepository.save(staffRequest)
    }

    fun updateStaffRequestStatus(requestId: Int, status: StaffRequest.Status): StaffRequest? {
        val staffRequest = staffRequestRepository.findById(requestId).orElse(null)
            ?: throw Exception("Staff request not found")
        if (staffRequest.status != StaffRequest.Status.PENDING) {
            throw Exception("Staff request is not pending")
        }
        staffRequest.let {
            it.status = status
            if (status == StaffRequest.Status.ACCEPTED) {
                staffRepository.save(
                    Staff(
                        user = staffRequest.user,
                        shop = staffRequest.shop,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                )
            }
            return staffRequestRepository.save(it)
        }
    }

    fun getRequestsByStatus(status: StaffRequest.Status): List<StaffRequest> {
        return staffRequestRepository.findByStatus(status)
    }
}
