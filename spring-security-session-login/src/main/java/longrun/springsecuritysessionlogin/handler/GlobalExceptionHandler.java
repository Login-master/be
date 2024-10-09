package longrun.springsecuritysessionlogin.handler;

import longrun.springsecuritysessionlogin.dto.response.ErrorResponse;
import longrun.springsecuritysessionlogin.exception.EmailNotFoundException;
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
    public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException e){
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
            break;// 일단 하나의 오류 정보만 담음
        }
        return ResponseEntity.status(errorCode.getStatus()).body(builder);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFoundException(EmailNotFoundException e){
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode);
    }


}
