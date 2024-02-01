package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_text", length = 100)
    private String reviewText; // review 글

    @Column(name = "member_rating")
    private Double memberRating; // 평점

    @Column(name = "member_rating_genre")
    private String memberRatingGenre; // 장르

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    // 생성 메서드
    public static Review createReview(String reviewText, Double memberRating, String memberRatingGenre){
        Review review = new Review();

        review.reviewText = reviewText;
        review.memberRating = memberRating;
        review.memberRatingGenre = memberRatingGenre;
        return review;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getReviewList().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getReviewList().add(this);
    }
}
