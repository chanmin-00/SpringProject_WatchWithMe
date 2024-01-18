package WatchWithMe.global.exception.dto;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException{

    private GlobalErrorCode globalErrorCode;

}
