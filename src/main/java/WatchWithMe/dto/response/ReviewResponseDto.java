package WatchWithMe.dto.response;

import WatchWithMe.domain.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {

    private String reviewText; // review 글

    private Double memberRating; // 평점

    private String memberRatingGenre; // 사용자 평가 장르

    private String title; // 영화 제목

    private String openYear; // 제작 연도

    private Double userRating; // 평균 평점

    private String genre; // 장르

    private String author; // 작성자

    public ReviewResponseDto(Review review){

        this.reviewText = review.getReviewText();
        this.memberRating = review.getMemberRating();
        this.memberRatingGenre = review.getMemberRatingGenre();
        this.title = review.getMovie().getTitle();
        this.openYear = review.getMovie().getOpenYear();
        this.userRating = review.getMovie().getUserRating();
        this.genre = review.getMovie().getGenre();
        this.author = review.getMember().getName();

    }
}
