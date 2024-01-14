package WatchWithMe.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T date;

    //성공 시 응답
    public static <T>ApiResponse<T> onSuccesss(String message, T result){
        return new ApiResponse<>(true, "OK", message, result);
    }

    //실패 시  응답
    public static <T>ApiResponse<T> onFailure(String message, T result){
        return new ApiResponse<>(false, "NO", message, result);
    }
}
