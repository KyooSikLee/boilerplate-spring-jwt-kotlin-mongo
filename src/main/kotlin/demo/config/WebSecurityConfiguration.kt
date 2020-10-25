package demo.config

import demo.security.jwt.AuthEntryPointJwt
import demo.security.jwt.AuthTokenFilter
import demo.security.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    lateinit var unauthorizedHandler: AuthEntryPointJwt

    @Autowired
    lateinit var authTokenFilter: AuthTokenFilter

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
    }


    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http {
            cors { }
            csrf { disable() }
            exceptionHandling {
                authenticationEntryPoint = unauthorizedHandler
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize("/api/auth/**", permitAll)
                authorize("/api/admin/**", hasAuthority("ROLE_ADMIN"))
                authorize(anyRequest, authenticated)
            }
        }
        //working on https://github.com/spring-projects/spring-security/issues/8316
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        // equivalent
//        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers("/api/auth/**").permitAll()
//                .anyRequest()
//                .authenticated()
//        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

}