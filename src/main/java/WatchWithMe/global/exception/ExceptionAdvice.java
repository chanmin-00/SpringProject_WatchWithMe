package WatchWithMe.global.exception;

import WatchWithMe.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /*
    @ExceptionHandler(value = {GlobalException.class})
    protected ApiResponse<String> handleException(GlobalException e) {
        log.error(e.getMessage() + ": " + e.getGlobalErrorCode());
        return ApiResponse.onFailure(e.getGlobalErrorCode(), "");
    }

     */
}
