package WatchWithMe.dto.request.review;

import jakarta.validation.constraints.*;

// 리뷰 수정 Dto
public record ChangeReviewRequestDto(

        @Size(max = 50, message = "리뷰 작성글은 50자 이내입니다")
        String reviewText, // review 글

        @NotNull(message = "필수 입력값입니다")
        @Max(value = 5, message = "최댓값은 5입니다")
        @Min(value = 0, message = "최솟값은 0입니다")
        Double memberRating, // 평점

        String memberRatingGenre// 장르

) {}
