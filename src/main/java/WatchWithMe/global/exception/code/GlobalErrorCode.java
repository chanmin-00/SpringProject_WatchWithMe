package WatchWithMe.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바립니다"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다"),
    _UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "COMMON401", "유효하지 않은 토큰입니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다"),
    _ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "존재하지 않는 계정입니다"),
    _NO_CONTENTS(HttpStatus.NO_CONTENT, "COMMON404", "요청한 데이터가 존재하지 않습니다"),

    // 이메일 중복 에러 코드
    _DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "COMMON400", "이미 등록된 이메일입니다"),
    // 비밀번호 일치 에러 코드
    _DIFF_PASSWORD(HttpStatus.BAD_REQUEST, "COMMON400", "비밀번호가 일치하지 않습니다"),
    // 이메일, 비밀번호 형식 에러 코드
    _METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "COMMON400", "요청 형식이 잘못되었습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
