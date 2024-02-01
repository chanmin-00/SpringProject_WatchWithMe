package WatchWithMe.repository.review;

import WatchWithMe.domain.Review;
import java.util.List;

public interface ReviewRepositoryCustom {

    /*
    내가 쓴 리뷰 조회
     */
    List<Review> searchByMember(Long memberId);

    /*
    영화 리뷰 조회
     */
    List<Review> searchByMovie(Long movieId);
}
