package com.tannguyen.ai.handler;

import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.dto.response.StandardResponse;
import com.tannguyen.ai.exception.ForbiddenException;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.exception.ResourceAlreadyExistsException;
import com.tannguyen.ai.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleNotFoundException(UnauthorizedException ex) {
        return ResponseFactory.error(HttpStatus.UNAUTHORIZED, "Login is required for access this data", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleNotFoundException(ForbiddenException ex) {
        return ResponseFactory.error(HttpStatus.FORBIDDEN, "You are not allow to access this data", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return ResponseFactory.error(HttpStatus.NOT_FOUND, "Data not found", ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseFactory.error(HttpStatus.CONFLICT, "Resource already existed", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleGeneric(Exception ex, WebRequest request) {
        return ResponseFactory.error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex.getMessage());
    }
}
