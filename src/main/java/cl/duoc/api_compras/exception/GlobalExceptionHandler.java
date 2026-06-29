package cl.duoc.api_compras.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> ResponseEnt(HttpStatus status, String mensaje, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);
        body.put("URI-RUTA", request.getDescription(false));

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> ValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Error de validación");
        return ResponseEnt(HttpStatus.BAD_REQUEST, mensaje, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> MalformedJson(HttpMessageNotReadableException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.BAD_REQUEST, "JSON mal formado u otro error de datos", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> BadRequest(IllegalArgumentException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> NotFoundElement(NoSuchElementException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> ConflictData(DataIntegrityViolationException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.CONFLICT, "Violación de integridad de datos", request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> MethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> NotFound(RuntimeException ex, WebRequest request) {
        return ResponseEnt(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> GeneralError(Exception ex, WebRequest request) {
        return ResponseEnt(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request);
    }

}
