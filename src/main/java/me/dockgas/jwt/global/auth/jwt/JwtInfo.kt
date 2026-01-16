package me.dockgas.jwt.global.auth.jwt

data class JwtInfo(
    val accessToken: String,
    val refreshToken: String
)
