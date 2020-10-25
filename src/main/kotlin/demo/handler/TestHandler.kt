package demo.handler

import demo.jwtUser
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok

@Component
class TestHandler {
    @PreAuthorize("hasRole('GENERAL')")
    fun userFunction(serverRequest: ServerRequest): ServerResponse {
        println(jwtUser())
        return ok().body("hello")
    }
}