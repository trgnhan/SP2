package com.nhan.sp2.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.awt.*;
import java.util.Date;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    // 404 - Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Bad Request",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "404 Response",
                                    summary = "Handle exception when resource not found",
                                    value = """
                                        {
                                          "timestamp": "2023-10-19T06:07:35.321+00:00",
                                          "status": 404,
                                          "path": "/api/v1/...",
                                          "error": "Not Found",
                                          "message": "{data} not found"
                                        }
                                        """
                            ))})
    })
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.error("========================= handleResourceNotFoundException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(NOT_FOUND.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(NOT_FOUND.getReasonPhrase()); // "Not Found"
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
    // 403
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "403 Response",
                                    summary = "Handle exception when forbidden",
                                    value = """
                                        {
                                          "timestamp": "2023-10-19T06:07:35.321+00:00",
                                          "status": 403,
                                          "path": "/api/v1/...",
                                          "error": "Forbidden",
                                          "message": "{data} ..."
                                        }
                                        """
                            ))})
    })
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        log.error("========================= handleAccessDeniedException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(FORBIDDEN.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(FORBIDDEN.getReasonPhrase()); // "Forbidden" tu choi truy cap
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
    // 401 - Unauthorized - Xac thuc dang nhap cho tai khoan / mat khau that bai
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "401 Response",
                                    summary = "Handle exception when Unauthorized",
                                    value = """
                                        {
                                          "timestamp": "2023-10-19T06:07:35.321+00:00",
                                          "status": 401,
                                          "path": "/api/v1/...",
                                          "error": "Unauthorized",
                                          "message": "Username or password is incorrect",
                                        }
                                        """
                            ))})
    })
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        log.error("========================= handleBadCredentialsException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(UNAUTHORIZED.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(UNAUTHORIZED.getReasonPhrase()); // "Unauthorized" sai xac thuc dang nhap
        errorResponse.setMessage("Username or password is incorrect");

        return errorResponse;
    }

    // 400 - Bad Request
    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        log.error("========================= handleValidationException : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            if (start != -1 && end != -1 && start < end) {
                message = message.substring(start + 1, end - 1);
            }
            errorResponse.setError("Payload Invalid");
        } else if (e instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse.setError("PathVariable Invalid");
        }

        errorResponse.setMessage(message);
        return errorResponse;
    }

    // 400 - Type Mismatch
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        log.error("========================= handleTypeMismatchException : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError("Type Mismatch");
        errorResponse.setMessage(String.format("Failed to convert value of type '%s' for parameter '%s'",
                e.getValue() != null ? e.getValue().getClass().getSimpleName() : "null", e.getName()));

        return errorResponse;
    }

    // 500 - Catch-all cho các ngoại lệ chưa được khai báo
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUncaughtException(Exception e, WebRequest request) {
        log.error("========================= Unhandled Exception", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(INTERNAL_SERVER_ERROR.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage("An unexpected error occurred: " + e.getMessage());

        return errorResponse;
    }
}