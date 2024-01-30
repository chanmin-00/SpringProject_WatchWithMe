package WatchWithMe.controller;

import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actor")
public class ActorController {

    private final ActorService actorService;

    /*
    영화 배우별 영화 검색
     */
    @PostMapping("/search")
    public ApiResponse search(@RequestBody ActorListRequestDto actorListRequestDto) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", actorService.searchMovieListByActor(actorListRequestDto));
    }
}
