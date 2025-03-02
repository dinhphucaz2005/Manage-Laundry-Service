package manage.laundry.service.controller

import manage.laundry.service.dto.request.StaffRequestDTO
import manage.laundry.service.entity.StaffRequest
import manage.laundry.service.service.StaffRequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/staff-requests")
class StaffRequestController(
    private val staffRequestService: StaffRequestService
) {

    @PostMapping
    fun createStaffRequest(@RequestBody request: StaffRequestDTO): ResponseEntity<Any> {
        val staffRequest = staffRequestService.createStaffRequest(request.userId, request.shopId)
        return ResponseEntity.ok(staffRequest)
    }

    @PutMapping("/{requestId}")
    fun updateStaffRequestStatus(
        @PathVariable requestId: Int,
        @RequestParam status: String
    ): ResponseEntity<Any> {
        val statusEnum = StaffRequest.Status.valueOf(status.uppercase())
        val updatedRequest = staffRequestService.updateStaffRequestStatus(requestId, statusEnum)
        return if (updatedRequest != null) {
            ResponseEntity.ok(updatedRequest)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/status/{status}")
    fun getRequestsByStatus(@PathVariable status: String): ResponseEntity<List<StaffRequest>> {
        val statusEnum = StaffRequest.Status.valueOf(status.uppercase())
        val requests = staffRequestService.getRequestsByStatus(statusEnum)
        return ResponseEntity.ok(requests)
    }
}
