package WatchWithMe.controller;

import WatchWithMe.dto.request.review.ChangeReviewRequestDto;
import WatchWithMe.dto.request.review.SearchReviewRequestDto;
import WatchWithMe.dto.request.review.WriteReviewRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "리뷰 작성", description = "이메일, 텍스트, 평점, 장르, movieId 입력 필요")
    public ApiResponse<Long> write(@RequestBody @Valid WriteReviewRequestDto writeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 쓰기에 성공하였습니다.", reviewService.write(writeReviewRequestDto));
    }

    // 리뷰 수정
    @PutMapping("/change/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "텍스트, 평점, 장르 입력 필요")
    public ApiResponse<Long> write(@PathVariable Long reviewId, @RequestBody @Valid ChangeReviewRequestDto changeReviewRequestDto){
        return ApiResponse.onSuccess("리뷰 수정에 성공하였습니다.", reviewService.change(reviewId, changeReviewRequestDto));
    }

    // 리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ApiResponse delete(@PathVariable Long reviewId){
        reviewService.delete(reviewId);
        return ApiResponse.onSuccess("리뷰 삭제에 성공하였습니다.", "");
    }

    // 내가 쓴 리뷰 조회
    @GetMapping("/findByMember/{memberId}")
    @Operation(summary = "자신이 쓴 리뷰 조회", description = "조회 페이지 입력  필요")
    public ApiResponse findByMember(@PathVariable Long memberId,@Valid @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMemberId(memberId, page));
    }

    // 영화별 리뷰 조회
    @GetMapping("/findByMovie/{movieId}")
    @Operation(summary = "영화 리뷰 조회", description = "조회 페이지 입력  필요")
    public ApiResponse findByMovie(@PathVariable Long movieId,@Valid  @RequestParam(value="page", defaultValue="1") int page){
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieId(movieId, page));
    }

    // 영화별 리뷰 조회, 평점 높음 순
    @GetMapping("/findByMovie/ratingDesc/{movieId}")
    @Operation(summary = "영화 리뷰 조회, 높은 평점순", description = "조회 페이지 입력  필요")
    public ApiResponse findByMovieRatingDesc(@PathVariable Long movieId,@Valid  @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieIdRatingDesc(movieId, page));
    }

    // 영화별 리뷰 조회, 평점 낮음 순
    @GetMapping("/findByMovie/ratingAsc/{movieId}")
    @Operation(summary = "영화 리뷰 조회, 낮은 평점순", description = "조회 페이지 입력  필요")
    public ApiResponse findByMovieRatingAsc(@PathVariable Long movieId,@Valid  @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.findReviewListByMovieIdRatingAsc(movieId, page));
    }

    // 리뷰 조건 검색, 리뷰 텍스트 검색
    @PostMapping("/search/reviewText")
    @Operation(summary =  "리뷰 조건 검색, 리뷰 내용 검색", description = "리뷰 내용 검색을 위한 텍스트 입력 필요")
    public ApiResponse searchReviewText(@Valid @RequestParam(value = "page", defaultValue = "1") int page, @Valid @RequestBody SearchReviewRequestDto searchReviewRequestDto) {
        return ApiResponse.onSuccess("리뷰 조회에 성공하였습니다", reviewService.searchReviewText(searchReviewRequestDto, page));
    }
}
