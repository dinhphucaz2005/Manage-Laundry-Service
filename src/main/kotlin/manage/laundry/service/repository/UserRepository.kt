package manage.laundry.service.repository

import manage.laundry.service.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Int> {

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?

    @Query("SELECT COUNT(u) FROM User u WHERE u.role IN :roles")
    fun countByRoleIn(roles: List<User.Role>): Int
}
