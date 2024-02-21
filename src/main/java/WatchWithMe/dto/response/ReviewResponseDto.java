package WatchWithMe.dto.response;

import WatchWithMe.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final String reviewText; // review 글

    private final Double memberRating; // 평점

    private final String memberRatingGenre; // 사용자 평가 장르

    private final String title; // 영화 제목

    private final String openYear; // 제작 연도

    private final Double userRating; // 평균 평점

    private final String genre; // 장르

    private String author; // 작성자

    public ReviewResponseDto(Review review){

        this.reviewText = review.getReviewText();
        this.memberRating = review.getMemberRating();
        this.memberRatingGenre = review.getMemberRatingGenre();
        this.title = review.getMovie().getTitle();
        this.openYear = review.getMovie().getOpenYear();
        this.userRating = review.getMovie().getUserRating();
        this.genre = review.getMovie().getGenre();
        if (review.getMember() != null)
            this.author = review.getMember().getName();
        else
            this.author = "";
    }
}
