package demo.security.services

import com.fasterxml.jackson.annotation.JsonIgnore
import demo.model.User
import demo.model.UserRole
import org.bson.types.ObjectId
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class JwtUser(
        val id: ObjectId,
        val email: String,
        @JsonIgnore
        private val password: String,
        val role: UserRole,
        private val authority: SimpleGrantedAuthority
) : UserDetails {

    companion object {
        fun build(user: User): JwtUser {
            val authority = SimpleGrantedAuthority(user.role.name)

            return JwtUser(
                    user.id,
                    user.email,
                    user.encryptedPassword,
                    user.role,
                    authority
            )
        }
    }

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return listOf(authority)
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}