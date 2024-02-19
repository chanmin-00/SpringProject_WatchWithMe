package WatchWithMe.repository.review;

import WatchWithMe.domain.Review;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReviewRepositoryCustom {

    // 내가 쓴 리뷰 조회
    List<Review> findByMember(Long memberId, Pageable pageable);

    // 영화 리뷰 조회
    List<Review> findByMovie(Long movieId, Pageable pageable);

    // 영화 리뷰 조회, 평점 높음순
    List<Review> findByMovieRatingDesc(Long movieId, Pageable pageable);

    // 영화 리뷰 조회, 평점 낮음순
    List<Review> findByMovieRatingAsc(Long movieId, Pageable pageable);

    // 리뷰 조건 검색, 리뷰 텍스트 검색
    List<Review> searchReviewText(String word);

}
