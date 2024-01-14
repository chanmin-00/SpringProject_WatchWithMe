package WatchWithMe.controller;

import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/update")
    public ApiResponse update(){
        try {
            movieService.updateMovieList();
        }
        catch (Exception e){
            return ApiResponse.onFailure("업데이트에 실패하였습니다.", "");
        }
        return ApiResponse.onSuccesss("업데이트에 성공하였습니다.", "");
    }
}
