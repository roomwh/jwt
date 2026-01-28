package me.dockgas.jwt.global.auth.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import me.dockgas.jwt.domain.user.domain.model.User
import me.dockgas.jwt.global.exception.CustomException
import me.dockgas.jwt.global.exception.JwtErrorCode
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.SignatureException
import java.util.Date
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils (
    private val jwtProperties: JwtProperties,
    private val userDetailsService: UserDetailsService
){
    companion object {
        const val TOKEN_TYPE_ACCESS = "access"
        const val TOKEN_TYPE_REFRESH = "refresh"
    }

    // secretKey를 만들어서 압축
    private val secretKey: SecretKey = SecretKeySpec(
        jwtProperties.secretKey.toByteArray(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().algorithm
    )

    fun generate(user: User) : JwtInfo {
        val accessToken = createToken(user, jwtProperties.accessExpired, TOKEN_TYPE_ACCESS)
        val refreshToken = createToken(user, jwtProperties.refreshExpired, TOKEN_TYPE_REFRESH)
        return JwtInfo("Bearer $accessToken", "Bearer $refreshToken")
    }

    // 토큰 생성
    private fun createToken(user: User, tokenExpire: Long, tokenType: String): String {
        val now = Date().time
        return Jwts.builder()
            .claim("id", user.id)
            .claim("email", user.email)
            .claim("role", user.role)
            .claim("type", tokenType) // 최소 정보만 입력
            .issuedAt(Date(now)) // 발급일
            .expiration(Date(now + tokenExpire)) // 만료일
            .signWith(secretKey) // secretKey 사용하여 암호화
            .compact()
    }

    // 토큰 검증
    fun checkTokenInfo(token: String) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
        } catch (e: ExpiredJwtException) { // 토큰 만료 (가장 많이 발생)
            throw CustomException(JwtErrorCode.JWT_TOKEN_EXPIRED)
        } catch (e: SignatureException) {
            throw CustomException(JwtErrorCode.JWT_TOKEN_SIGNATURE_ERROR)
        } catch (e: MalformedJwtException) {
            throw CustomException(JwtErrorCode.JWT_TOKEN_ERROR)
        } catch (e: UnsupportedJwtException) {
            throw CustomException(JwtErrorCode.JWT_TOKEN_UNSUPPORTED_ERROR)
        } catch (e: Exception) {
            throw CustomException(JwtErrorCode.JWT_UNKNOWN_EXCEPTION)
        }
    }

    // 토큰 Bearer 제거
    fun getToken(token: String): String = token.removePrefix("Bearer ")

    // payload에 있는 email 값 추출
    fun getUserEmail(token: String): String =
        Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).payload.get("email", String::class.java)

    // payload에 있는 type 값 추출하여 refresh토큰인지 확인
    fun isRefreshToken(token: String): Boolean {
        val type = Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).payload.get("type", String::class.java)
        return type == TOKEN_TYPE_REFRESH
    }

    // 권한 검증
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val userDetails = userDetailsService.loadUserByUsername(getUserEmail(getToken(token)))
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    // refresh 토큰을 access 토큰으로 발급
    fun refreshAccessToken(user: User): String =
        "Bearer " + createToken(user, jwtProperties.accessExpired, TOKEN_TYPE_ACCESS)
}