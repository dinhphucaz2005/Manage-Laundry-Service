package manage.laundry.service.repository

import manage.laundry.service.entity.SystemNotification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SystemNotificationRepository : JpaRepository<SystemNotification, Int>