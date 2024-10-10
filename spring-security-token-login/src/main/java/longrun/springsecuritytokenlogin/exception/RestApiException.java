package longrun.springsecuritytokenlogin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예외 클래스
 */
@RequiredArgsConstructor
@Getter
public class RestApiException extends RuntimeException {
  private final ErrorCode errorCode;
}
