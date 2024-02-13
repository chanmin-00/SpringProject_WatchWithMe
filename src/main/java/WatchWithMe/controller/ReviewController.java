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

    // 리뷰 작성
    @PutMapping("/write")
    public ApiResponse<Long> write(@RequestBody @Valid WriteReviewRequestDto writeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 쓰기에 성공하였습니다.", reviewService.write(writeReviewRequestDto));
    }

    // 리뷰 수정
    @PutMapping("/change/{reviewId}")
    public ApiResponse<Long> write(@PathVariable Long reviewId, @RequestBody @Valid ChangeReviewRequestDto changeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 수정에 성공하였습니다.", reviewService.change(reviewId, changeReviewRequestDto));
    }

    // 리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    public ApiResponse delete(@PathVariable Long reviewId){
        reviewService.delete(reviewId);
        return ApiResponse.onSuccess("리뷰 삭제에 성공하였습니다.", "");
    }

    // 내가 쓴 리뷰 조회
    @GetMapping("/findByMember/{memberId}")
    public ApiResponse findByMember(@PathVariable Long memberId, @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMemberId(memberId, page));
    }

    // 영화별 리뷰 조회
    @GetMapping("/findByMovie/{movieId}")
    public ApiResponse findByMovie(@PathVariable Long movieId, @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieId(movieId, page));
    }

    // 영화별 리뷰 조회, 평점 높음 순
    @GetMapping("/findByMovie/ratingDesc/{movieId}")
    public ApiResponse findByMovieRatingDesc(@PathVariable Long movieId, @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieIdRatingDesc(movieId, page));
    }

    // 영화별 리뷰 조회, 평점 낮음 순
    @GetMapping("/findByMovie/ratingAsc/{movieId}")
    public ApiResponse findByMovieRatingAsc(@PathVariable Long movieId, @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieIdRatingAsc(movieId, page));
    }
}
