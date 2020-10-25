package demo.payload

import demo.model.UserRole
import org.bson.types.ObjectId

data class JwtResponse(
        val jwt: String,
        val id: ObjectId,
        val email: String,
        val role: UserRole
)