// package com.assignment.job_scheduler.exception;

// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;

// import java.time.LocalDateTime;
// import java.util.LinkedHashMap;
// import java.util.Map;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(RuntimeException.class)
//     public ResponseEntity<Object> handleRuntime(RuntimeException ex, HttpServletRequest request) {
//         ex.printStackTrace();  // 🔍 log full stack trace to console
//         return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
//     }

//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest request) {
//         ex.printStackTrace();  // 🔍 log full stack trace to console
//         return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", request.getRequestURI());
//     }

//     private ResponseEntity<Object> buildResponse(HttpStatus status, String message, String path) {
//         Map<String, Object> body = new LinkedHashMap<>();
//         body.put("timestamp", LocalDateTime.now());
//         body.put("status", status.value());
//         body.put("error", status.getReasonPhrase());
//         body.put("message", message);
//         body.put("path", path);
//         return new ResponseEntity<>(body, status);
//     }
// }
