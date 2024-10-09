package longrun.springsecuritysessionlogin.handler;

import longrun.springsecuritysessionlogin.dto.response.ErrorResponse;
import longrun.springsecuritysessionlogin.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // validated 시 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidException(MethodArgumentNotValidException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getMessage());
    }
}
