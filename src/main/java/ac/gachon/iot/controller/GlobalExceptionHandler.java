package ac.gachon.iot.controller;

import ac.gachon.iot.dto.ErrorResponse;
import ac.gachon.iot.exception.InvalidCredentialsException;
import ac.gachon.iot.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 이거는 InvalidCrednetialsException 이 발생했을 때 해당 handelInvalidCredential 메서드가 실행이 되고 build 메서드의 반환값을 return 한다.
    // 타입은 ResponseEntity<ErrorResponse> 이렇게 되어 있음
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredential(InvalidCredentialsException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    // 이거는 SpringMVC 내부적으로 발생하는 예외를 동일한 형식으로 만들기 위한 메서드이다
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if (body instanceof ErrorResponse) {
            return new ResponseEntity<>(body, headers, statusCode);
        }
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        ErrorResponse wrapped = makeBody(status, ex.getMessage(), pathOf(request), null);
        return new ResponseEntity<>(wrapped, headers, statusCode);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, String path,
                                                List<ErrorResponse.FieldError> fieldErrors) {
        return ResponseEntity.status(status).body(makeBody(status, message, path, fieldErrors));
    }

    private ErrorResponse makeBody(HttpStatus status, String message, String path,
                                   List<ErrorResponse.FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .build();
    }

    private String pathOf(WebRequest request) {
        if (request instanceof ServletWebRequest swr) {
            return swr.getRequest().getRequestURI();
        }
        return null;
    }

}
