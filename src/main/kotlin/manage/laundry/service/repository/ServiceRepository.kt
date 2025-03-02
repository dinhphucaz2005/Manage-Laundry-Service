package manage.laundry.service.repository

import manage.laundry.service.entity.Service
import manage.laundry.service.entity.Shop
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository : JpaRepository<Service, Int>
