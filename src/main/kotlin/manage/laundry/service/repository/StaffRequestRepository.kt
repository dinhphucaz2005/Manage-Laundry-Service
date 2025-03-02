package manage.laundry.service.repository

import manage.laundry.service.entity.StaffRequest
import org.springframework.data.jpa.repository.JpaRepository

interface StaffRequestRepository : JpaRepository<StaffRequest, Int> {

    fun findByStatus(status: StaffRequest.Status): List<StaffRequest>
}