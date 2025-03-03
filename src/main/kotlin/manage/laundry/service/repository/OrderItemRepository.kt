package manage.laundry.service.repository

import manage.laundry.service.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository: JpaRepository<OrderItem, Int>