package WatchWithMe.controller;

import WatchWithMe.dto.request.movie.MovieListRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    // 영화 단건 조회
    @GetMapping("/findOne/{movieId}")
    public ApiResponse findMovie(@PathVariable Long movieId) {
        return ApiResponse.onSuccess("영화 조회에 성공했습니다.", movieService.getMovieById(movieId));
    }

    // 영화 전체 조회, 페이징
    @GetMapping("/findList")
    public ApiResponse findMovieList(@RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 목록 조회에 성공했습니다", movieService.getMovieList(page));
    }

    // 영화 전체 조회, 평점 높음 순 조회
    @GetMapping("/find/ratingDescList")
    public ApiResponse findRatingDescMovieList(@RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 목록 조회에 성공했습니다", movieService.getUserRatingDescMovieList(page));
    }

    // 영화 전체 조회, 평점 낮음 순 조회
    @GetMapping("/find/ratingAscList")
    public ApiResponse findRatingAscMovieList(@RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 목록 조회에 성공했습니다", movieService.getUserRatingAscMovieList(page));
    }

    // 영화 전체 조회, 리뷰 많음 순 조회
    @GetMapping("/find/reviewMostList")
    public ApiResponse findReviewMostList(@RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.onSuccess("영화 목록 조회에 성공했습니다", movieService.getReviewMostMovieList(page));
    }

    // 영화 조건 검색
    @GetMapping("/search")
    public ApiResponse search(@RequestBody MovieListRequestDto movieListRequestDto, @RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", movieService.searchMovieList(movieListRequestDto, page));
    }
}
