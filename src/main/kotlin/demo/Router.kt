package demo

import demo.handler.AuthHandler
import demo.handler.TestHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.router

@Component
class Router(
        val authHandler: AuthHandler,
        val testHandler: TestHandler
) {
    @Bean
    fun routes() = router {
        "/api".nest {
            "/auth".nest {
                POST("/register", authHandler::registerUser)
                GET("/signin", authHandler::authenticateUser)
            }
            "/test".nest {
                GET("/user", testHandler::userFunction)
            }
        }
    }
}