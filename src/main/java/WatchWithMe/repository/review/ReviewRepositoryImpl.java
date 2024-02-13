package WatchWithMe.repository.review;

import WatchWithMe.domain.Review;
import WatchWithMe.repository.MemberRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static WatchWithMe.domain.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;

    @Override
    public List<Review> searchByMember(Long memberId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMemberEq(memberId))
                .orderBy(review.reviewId.desc()) // 리뷰 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Review> searchByMovie(Long movieId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.reviewId.desc()) // 리뷰 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 리뷰 평점 높음순 조회
    @Override
    public List<Review> searchByMovieRatingDesc(Long movieId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.memberRating.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 리뷰 평점 낮음순 조회
    public List<Review> searchByMovieRatingAsc(Long movieId, Pageable pageable) {

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
                .orderBy(review.memberRating.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
}
