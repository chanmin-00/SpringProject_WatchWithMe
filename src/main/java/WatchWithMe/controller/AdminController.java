package WatchWithMe.controller;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final MovieService movieService;

    // 영화 정보 업데이트
    @PostMapping("update/movie")
    @Operation(summary = "영화 정보 업데이트", description = "업데이트 후 DB 저장")
    public ApiResponse update(){
        try {
            movieService.updateMovieList();
        }
        catch (Exception e){
            return ApiResponse.onFailure(GlobalErrorCode._INTERNAL_SERVER_ERROR, "");
        }
        return ApiResponse.onSuccess("업데이트에 성공하였습니다.", "");
    }

}
