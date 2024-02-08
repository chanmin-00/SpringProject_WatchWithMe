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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Review> searchByMovie(Long movieId, Pageable pageable){

        return queryFactory.select(review).from(review)
                .where(reviewMovieEq(movieId))
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
