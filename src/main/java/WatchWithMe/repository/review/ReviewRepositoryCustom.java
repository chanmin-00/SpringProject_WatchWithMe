package WatchWithMe.repository.review;

import WatchWithMe.domain.Review;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReviewRepositoryCustom {

    // 내가 쓴 리뷰 조회
    List<Review> searchByMember(Long memberId, Pageable pageable);

    // 영화 리뷰 조회
    List<Review> searchByMovie(Long movieId, Pageable pageable);

    // 영화 리뷰 조회, 평점 높음순
    List<Review> searchByMovieRatingDesc(Long movieId, Pageable pageable);

    // 영화 리뷰 조회, 평점 낮음순
    List<Review> searchByMovieRatingAsc(Long movieId, Pageable pageable);
}
