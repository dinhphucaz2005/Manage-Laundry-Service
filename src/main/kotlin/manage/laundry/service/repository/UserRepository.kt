package manage.laundry.service.repository

import manage.laundry.service.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?

}
