package me.dockgas.jwt.domain.user.domain.mapper

import me.dockgas.jwt.domain.user.domain.entity.UserEntity
import me.dockgas.jwt.domain.user.domain.enums.UserRoles
import me.dockgas.jwt.domain.user.domain.model.User
import me.dockgas.jwt.global.common.Mapper
import org.springframework.stereotype.Component

@Component
class UserMapper : Mapper<User, UserEntity> {
    override fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            email = entity.email,
            name = entity.name,
            password = entity.password,
            role = entity.role
        )
    }

    override fun toEntity(domain: User): UserEntity {
        return UserEntity(
            email = domain.email,
            name = domain.name,
            password = domain.password,
            role = domain.role
        )
    }

}