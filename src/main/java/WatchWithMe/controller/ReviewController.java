package WatchWithMe.controller;

import WatchWithMe.dto.request.WriteReviewRequestDto;
import WatchWithMe.dto.response.ReviewResponseDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PutMapping("/write")
    public ApiResponse<Long> write(@RequestBody @Valid WriteReviewRequestDto writeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 쓰기에 성공하였습니다.", reviewService.write(writeReviewRequestDto));
    }

    @GetMapping("/searchByMember/{memberId}")
    public ApiResponse searchByMember(@PathVariable Long memberId){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMemberId(memberId));
    }

    @GetMapping("/searchByMovie/{movieId}")
    public ApiResponse searchByMovie(@PathVariable Long movieId){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieId(movieId));
    }
}
