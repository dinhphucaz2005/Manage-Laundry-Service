package manage.laundry.service.service


import manage.laundry.service.dto.request.CreateServiceRequest
import manage.laundry.service.entity.Service
import manage.laundry.service.repository.ServiceRepository
import manage.laundry.service.repository.ShopRepository
import java.time.LocalDateTime

@org.springframework.stereotype.Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val shopRepository: ShopRepository
) {

    fun addServiceToShop(request: CreateServiceRequest): Service {
        val shop = shopRepository.findById(request.shopId).orElseThrow { Exception("Shop not found") }

        val service = Service(
            shop = shop,
            name = request.name,
            description = request.description,
            price = request.price,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return serviceRepository.save(service)
    }
}
