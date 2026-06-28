package cl.duoc.api_compras.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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


        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Object> BadRequest(IllegalArgumentException ex, WebRequest request) {
            return ResponseEnt(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
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
