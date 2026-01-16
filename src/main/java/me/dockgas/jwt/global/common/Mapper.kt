package me.dockgas.jwt.global.common

interface Mapper<D, E> {
    fun toDomain(entity: E): D
    fun toEntity(domain: D): E
}