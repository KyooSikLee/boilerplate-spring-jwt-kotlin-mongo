package demo.handler

import demo.model.User
import demo.model.UserRole
import demo.payload.JwtResponse
import demo.payload.LoginRequest
import demo.payload.RegisterRequest
import demo.repository.UserRepository
import demo.security.jwt.JwtUtils
import demo.security.services.JwtUser
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok

@Component
class AuthHandler(
        val authenticationManager: AuthenticationManager,
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder,
        val jwtUtils: JwtUtils
) {

    fun authenticateUser(serverRequest: ServerRequest): ServerResponse {
        val loginRequest = serverRequest.body(LoginRequest::class.java)

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val accessToken = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as JwtUser


        return ok().body(JwtResponse(accessToken, userDetails.id, userDetails.email, userDetails.role))
    }

    fun registerUser(serverRequest: ServerRequest): ServerResponse {
        val registerRequest = serverRequest.body(RegisterRequest::class.java)
        val user = User(
                email = registerRequest.email,
                encryptedPassword = passwordEncoder.encode(registerRequest.password),
                role = UserRole.ROLE_GENERAL
        )
        return ok().body(userRepository.save(user))
    }
}