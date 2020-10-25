package demo

import demo.security.services.JwtUser
import org.springframework.security.core.context.SecurityContextHolder


fun jwtUser() = SecurityContextHolder.getContext().authentication.principal as JwtUser