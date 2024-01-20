package WatchWithMe.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    _NO_CONTENTS(HttpStatus.NO_CONTENT, "204", "요청한 데이터가 존재하지 않습니다"),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바립니다"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다"),
    _ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "4004", "존재하지 않는 계정입니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
