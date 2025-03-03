package manage.laundry.service.repository

import manage.laundry.service.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int>
