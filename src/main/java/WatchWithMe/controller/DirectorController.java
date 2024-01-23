package WatchWithMe.controller;

import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/director")
public class DirectorController {

    private final DirectorService directorService;

    /*
    영화 감독별 영화 검색
     */
    @PostMapping("/search")
    public ApiResponse search(@RequestBody DirectorListRequestDto directorListRequestDto) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", directorService.searchMovieListByDirector(directorListRequestDto));
    }
}
