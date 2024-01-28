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
@RequestMapping("/user")
public class MovieController {

    private final MovieService movieService;

    /*
    영화 검색
     */
    @PostMapping("/movie/search")
    public ApiResponse search(@RequestBody MovieListRequestDto movieListRequestDto) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", movieService.searchMovieList(movieListRequestDto));
    }
}
