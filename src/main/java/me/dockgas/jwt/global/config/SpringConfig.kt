package me.dockgas.jwt.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
open class SpringConfig {
    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder() // 패스워드 암호화

    @Bean
    open fun objectMapper(): ObjectMapper = ObjectMapper()
}