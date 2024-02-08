package WatchWithMe.controller;

import WatchWithMe.dto.request.review.ChangeReviewRequestDto;
import WatchWithMe.dto.request.review.WriteReviewRequestDto;
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

    @PutMapping("/change/{reviewId}")
    public ApiResponse<Long> write(@PathVariable Long reviewId, @RequestBody @Valid ChangeReviewRequestDto changeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 수정에 성공하였습니다.", reviewService.change(reviewId, changeReviewRequestDto));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ApiResponse delete(@PathVariable Long reviewId){
        reviewService.delete(reviewId);
        return ApiResponse.onSuccess("리뷰 삭제에 성공하였습니다.", "");
    }

    @GetMapping("/searchByMember/{memberId}")
    public ApiResponse searchByMember(@PathVariable Long memberId, @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMemberId(memberId, page));
    }

    @GetMapping("/searchByMovie/{movieId}")
    public ApiResponse searchByMovie(@PathVariable Long movieId, @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieId(movieId, page));
    }
}
