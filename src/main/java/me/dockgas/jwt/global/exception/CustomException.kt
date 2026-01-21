package me.dockgas.jwt.global.exception

import java.lang.RuntimeException

class CustomException(
    val customErrorCode: CustomErrorCode
) : RuntimeException() {
}