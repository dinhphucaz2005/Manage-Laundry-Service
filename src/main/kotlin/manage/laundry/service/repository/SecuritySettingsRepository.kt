package manage.laundry.service.repository

import manage.laundry.service.entity.SecuritySettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SecuritySettingsRepository : JpaRepository<SecuritySettings, Int>