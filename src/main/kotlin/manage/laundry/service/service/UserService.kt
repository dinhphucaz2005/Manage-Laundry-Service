package manage.laundry.service.service


import manage.laundry.service.dto.request.CreateUserRequest
import manage.laundry.service.entity.User
import manage.laundry.service.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(request: CreateUserRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email '${request.email}' is already in use")
        }
        val user = User(
            name = request.name,
            email = request.email,
            password = request.password, // TODO: Hash password when deploying
            phone = request.phone,
            role = request.role
        )
        return userRepository.save(user)
    }

    fun getUserById(id: Int): User {
        return userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User with id $id not found") }
    }
}
