package WatchWithMe.global.exception;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // GlobalException 예외 처리 핸들러
    @ExceptionHandler(value = {GlobalException.class})
    protected ApiResponse<String> handleGlobalException(GlobalException e) {
        log.error(e.getMessage() + ": " + e.getGlobalErrorCode());
        return ApiResponse.onFailure(e.getGlobalErrorCode(), "");
    }

    // MethodArgumentNotValidException 예외 처리 핸들러
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(ApiResponse.onFailure(GlobalErrorCode._METHOD_ARGUMENT_NOT_VALID, ""));
    }

}
