package com.hubelias.parkingmeter.port.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
private class ExceptionHandlers {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    @ResponseBody
    fun handleBadRequest(exception: Exception): ErrorMessage {
        return createErrorMessage(exception)
    }

    private fun createErrorMessage(exception: Exception) =
            ErrorMessage(exception.javaClass.simpleName, exception.message)

    data class ErrorMessage(val error: String, val message: String?)
}
