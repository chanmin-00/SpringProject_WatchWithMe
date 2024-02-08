package WatchWithMe.controller;

import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actor")
public class ActorController {

    private final ActorService actorService;

    /*
    영화 배우별 영화 검색
     */
    @GetMapping("/search")
    public ApiResponse search(@RequestBody ActorListRequestDto actorListRequestDto, @RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", actorService.searchMovieListByActor(actorListRequestDto, page));
    }
}
