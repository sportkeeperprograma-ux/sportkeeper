package com.gimapp.sportkeeper.web.error


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ApiExceptionHandler {
    data class ApiError(val message: String)


    @ExceptionHandler(NoSuchElementException::class)
    fun notFound(ex: NoSuchElementException) = ResponseEntity(ApiError(ex.message ?: "Not found"), HttpStatus.NOT_FOUND)


    @ExceptionHandler(IllegalStateException::class)
    fun conflict(ex: IllegalStateException) = ResponseEntity(ApiError(ex.message ?: "Conflict"), HttpStatus.CONFLICT)


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun badRequest(ex: MethodArgumentNotValidException) = ResponseEntity(ApiError("Invalid request"), HttpStatus.BAD_REQUEST)
}