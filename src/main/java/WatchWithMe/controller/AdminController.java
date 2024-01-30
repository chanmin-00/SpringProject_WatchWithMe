package WatchWithMe.controller;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final MovieService movieService;

    /*
    영화 정보 업데이트
     */
    @PostMapping("movie/update")
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
