package me.dockgas.jwt.domain.user.exception

import me.dockgas.jwt.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class UserErrorCode(
    override val status: HttpStatus,
    override val state: String,
    override val message: String
) : CustomErrorCode { // CustomErrorCode 상속
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", message = "유저를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "CONFLICT", message = "이미 존재하는 이메일입니다."),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message = "이메일 또는 비밀번호가 잘못되었습니다.")
}