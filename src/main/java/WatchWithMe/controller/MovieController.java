package WatchWithMe.controller;

import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/update")
    public void update(){ //인자 수정 필요
        try {
            movieService.updateMovieList("20240112");
        }
        catch (Exception e){

        }
    }
}
