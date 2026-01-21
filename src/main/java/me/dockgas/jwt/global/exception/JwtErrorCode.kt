package me.dockgas.jwt.global.exception

import org.springframework.http.HttpStatus

enum class JwtErrorCode(
    override val status: HttpStatus,
    override val state: String,
    override val message: String
) : CustomErrorCode {
    JWT_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "FORBIDDEN", "토큰이 만료되었어요"),
    JWT_TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "토큰의 서명이 일치하지 않아요"),
    JWT_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "토큰이 올바르지 않아요"),
    JWT_TOKEN_UNSUPPORTED_ERROR(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "지원하지 않는 토큰이에요"),
    JWT_TOKEN_ILL_EXCEPTION(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "JWT 처리 중 오류가 발생했어요"),
    JWT_UNKNOWN_EXCEPTION(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "알 수 없는 오류가 발생했어요"),
    JWT_EMPTY_EXCEPTION(HttpStatus.FORBIDDEN, "FORBIDDEN", "토큰을 넣어주세요"),
    JWT_INVALID_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 토큰 타입입니다"),
    JWT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없어요")
}