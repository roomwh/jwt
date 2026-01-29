package me.dockgas.jwt.global.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() { // OncePerRequestFilter : 하나의 요청에 대해 필터가 한 번만 실행되도록 보장하는 Spring Security의 특별한 필터
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = request.getHeader("Authorization")

        if (!token.isNullOrEmpty()) {
            jwtUtils.checkTokenInfo(token) // 제대로 된 토큰인지 검증

            // SecurityContextHolder : 현재 실행 중인 스레드에 SecurityContext를 연결해 주는 역할
            SecurityContextHolder.getContext().authentication = jwtUtils.getAuthentication(token) // 실제 유저로 판단
        }

        // 현재 필터의 처리를 마치고 다음 필터체인으로
        filterChain.doFilter(request, response)
    }
}