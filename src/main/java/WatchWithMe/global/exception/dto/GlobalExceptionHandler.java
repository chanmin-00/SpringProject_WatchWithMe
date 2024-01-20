package WatchWithMe.global.exception.dto;

import WatchWithMe.global.exception.code.GlobalErrorCode;

public class GlobalExceptionHandler extends GlobalException{
    public GlobalExceptionHandler(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}
