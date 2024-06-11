package team9502.sinchulgwinong.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleApiException(ApiException ex) {
        log.error("API 예외 발생: ", ex);
        return buildResponse(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("런타임 예외 발생: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("잘못된 인자 예외 발생: ", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.INTERNAL_BAD_REQUEST.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("메서드 인자가 유효하지 않음 예외 발생: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        GlobalApiResponse<Map<String, String>> response = GlobalApiResponse.of(ErrorCode.VALIDATION_ERROR.getMessage(), errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private <T> ResponseEntity<GlobalApiResponse<T>> buildResponse(HttpStatus status, String message) {
        GlobalApiResponse<T> response = GlobalApiResponse.of(message, null);
        return new ResponseEntity<>(response, status);
    }
}
