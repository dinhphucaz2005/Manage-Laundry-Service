package manage.laundry.service.repository

import manage.laundry.service.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Int> {
}