package WatchWithMe.controller;

import WatchWithMe.dto.request.MovieListRequestDto;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    /*
    영화 정보 업데이트
     */
    @PostMapping("/update")
    public ApiResponse update(){
        try {
            movieService.updateMovieList();
        }
        catch (Exception e){
            return ApiResponse.onFailure(GlobalErrorCode._INTERNAL_SERVER_ERROR, "");
        }
        return ApiResponse.onSuccess("업데이트에 성공하였습니다.", "");
    }

    /*
    영화 검색
     */
    @PostMapping("/search")
    public ApiResponse search(@RequestBody MovieListRequestDto movieListRequestDto) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", movieService.searchMovieList(movieListRequestDto));
    }
}
