package demo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
        @Id
        val id: ObjectId = ObjectId(),
        val email: String,
        val name: String? = null,
        val encryptedPassword: String,
        val role: UserRole
)


enum class UserRole {
    ROLE_ADMIN,
    ROLE_GENERAL
}