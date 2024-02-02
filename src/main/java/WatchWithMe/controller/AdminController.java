package WatchWithMe.controller;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MovieService;
import WatchWithMe.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final MovieService movieService;
    private final MemberService memberService;

    /*
    영화 정보 업데이트
     */
    @PostMapping("update/movie")
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
    선호 장르 업데이트
    */
    @PutMapping("/update/favoriteGenre/{memberId}")
    public ApiResponse<Long> updateFavoriteGenre(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("선호 장르 업데이트에 성공하였습니다", memberService.updateFavoriteGenre(memberId));
    }
}
