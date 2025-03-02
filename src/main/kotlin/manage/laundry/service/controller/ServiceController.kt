package manage.laundry.service.controller


import jakarta.validation.Valid
import manage.laundry.service.dto.request.CreateServiceRequest
import manage.laundry.service.entity.Service
import manage.laundry.service.service.ServiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/services")
class ServiceController(
    private val serviceService: ServiceService
) {

    @PostMapping
    fun addService(@RequestBody @Valid request: CreateServiceRequest): ResponseEntity<Service> {
        val service = serviceService.addServiceToShop(request)
        return ResponseEntity.ok(service)
    }
}
