package me.dockgas.jwt.global.auth.jwt

import me.dockgas.jwt.domain.user.domain.UserRepository
import me.dockgas.jwt.domain.user.domain.mapper.UserMapper
import me.dockgas.jwt.domain.user.exception.UserErrorCode
import me.dockgas.jwt.global.exception.CustomException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserDetailsService { // UserDetailsService 상속
    // 이메일로 유저가 있는지 확인 (있으면 값을 받아와야 하므로)
    override fun loadUserByUsername(email: String): UserDetails {
        val userEntity = userRepository.findByEmail(email)
            ?: throw CustomException(UserErrorCode.USER_NOT_FOUND) // 조회했을 때 이메일이 없으면 exception 발생
        return JwtUserDetails(userMapper.toDomain(userEntity))
    }
}