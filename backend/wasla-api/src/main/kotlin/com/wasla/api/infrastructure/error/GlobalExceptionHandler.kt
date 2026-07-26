package com.wasla.api.infrastructure.error

import com.wasla.api.infrastructure.context.RequestContextHolder
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.MethodArgumentNotValidException

@RestControllerAdvice
class GlobalExceptionHandler(
    private val requestContextHolder: RequestContextHolder,
) {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(WaslaProblemException::class)
    fun handleKnownProblem(
        ex: WaslaProblemException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail(
            type = ex.problemType,
            title = HttpStatus.valueOf(ex.httpStatus).reasonPhrase,
            status = ex.httpStatus,
            code = ex.problemCode,
            detail = ex.safeDetail,
            instance = request.requestURI,
            requestId = currentRequestId(),
            retryable = ex.retryable,
            fields = ex.fields,
        )
        return ResponseEntity.status(ex.httpStatus)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val fields = ex.bindingResult.fieldErrors.map {
            ProblemDetail.FieldError(
                field = it.field,
                code = it.code ?: "invalid",
                message = it.defaultMessage ?: "Invalid value",
            )
        }
        val problem = ProblemDetail(
            type = "https://errors.wasla.example/common/validation-failed",
            title = "Validation failed",
            status = 400,
            code = "validation_failed",
            detail = "One or more fields failed validation.",
            instance = request.requestURI,
            requestId = currentRequestId(),
            retryable = false,
            fields = fields,
        )
        return ResponseEntity.badRequest()
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem)
    }

    /**
     * Catch-all for anything unexpected: SQLException, NPE, third-party client
     * errors, etc. Deliberately logs the real exception server-side but never
     * forwards its message to the client (rule 8.7).
     */
    @ExceptionHandler(Exception::class)
    fun handleUnexpected(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val requestId = currentRequestId()
        logger.error("Unexpected exception for request {}", requestId, ex)

        val problem = ProblemDetail(
            type = "https://errors.wasla.example/common/internal-error",
            title = "Internal Server Error",
            status = 500,
            code = "internal_error",
            detail = "An unexpected error occurred. Reference: $requestId",
            instance = request.requestURI,
            requestId = requestId,
            retryable = true,
        )
        return ResponseEntity.status(500)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(problem)
    }

    private fun currentRequestId(): String =
        requestContextHolder.get()?.requestId ?: "req_unknown"
}
