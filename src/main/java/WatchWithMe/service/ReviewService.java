package WatchWithMe.service;

import WatchWithMe.domain.Member;
import WatchWithMe.domain.Movie;
import WatchWithMe.domain.Review;
import WatchWithMe.dto.request.WriteReviewRequestDto;
import WatchWithMe.dto.response.ReviewResponseDto;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.repository.review.ReviewRepository;
import WatchWithMe.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    /*
    리뷰 DB 저장
     */
    public Long write(WriteReviewRequestDto writeReviewRequestDto){

        String email = writeReviewRequestDto.email();
        String reviewText = writeReviewRequestDto.reviewText();
        Double memberRating = writeReviewRequestDto.memberRating();
        String  memberRatingGenre = writeReviewRequestDto.memberRatingGenre();
        Long movieId = writeReviewRequestDto.movieId();

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Review review = Review.createReview(reviewText, memberRating, memberRatingGenre);
        review.setMember(member);
        review.setMovie(movie);
        reviewRepository.save(review);

        return review.getReviewId();
    }

    /*
    내가 쓴 리뷰 조회
     */
    public List<ReviewResponseDto> findReviewListByMemberId(Long memberId){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        List<Review> reviewList = reviewRepository.searchByMember(memberId);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    /*
    영화 리뷰 조회
     */
    public List<ReviewResponseDto> findReviewListByMovieId(Long movieId){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        List<Review> reviewList = reviewRepository.searchByMovie(movieId);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

}
