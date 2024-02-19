package WatchWithMe.repository.review;

import WatchWithMe.domain.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static WatchWithMe.domain.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findByMember(Long memberId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMemberEq(memberId))
                .orderBy(review.reviewId.desc()) // 리뷰 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Review> findByMovie(Long movieId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.reviewId.desc()) // 리뷰 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 리뷰 평점 높음순 조회
    @Override
    public List<Review> findByMovieRatingDesc(Long movieId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.memberRating.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 리뷰 평점 낮음순 조회
    @Override
    public List<Review> findByMovieRatingAsc(Long movieId, Pageable pageable) {

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.memberRating.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 리뷰 조건 검색, 리뷰 텍스트 검색
    @Override
    public List<Review> searchReviewText(String word) {

        return queryFactory.select(review).from(review)
                .where(reviewTextLike(word))
                .orderBy(review.reviewId.desc())
                .fetch();
    }

    private BooleanExpression reviewMemberEq(Long memberId){

        if (memberId == null)
            return null;

        return review.member.memberId.eq(memberId);
    }

    private BooleanExpression reviewMovieEq(Long movieId){

        if (movieId == null)
            return null;

        return review.movie.movieId.eq(movieId);
    }

    private BooleanExpression reviewTextLike(String searchText){

        if (searchText == null || searchText.isEmpty())
            return null;

        return review.reviewText.like("%" + searchText + "%");
    }

    private BooleanExpression reviewMemberRatingGenreLike(String searchText){

        if (searchText == null || searchText.isEmpty())
            return null;

        return review.memberRatingGenre.like("%" + searchText + "%");
    }
}
