package WatchWithMe.service;

import WatchWithMe.domain.Member;
import WatchWithMe.domain.Movie;
import WatchWithMe.domain.Review;
import WatchWithMe.dto.request.review.ChangeReviewRequestDto;
import WatchWithMe.dto.request.review.WriteReviewRequestDto;
import WatchWithMe.dto.response.ReviewResponseDto;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.repository.review.ReviewRepository;
import WatchWithMe.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    리뷰 수정
     */
    public Long change(Long reviewId, ChangeReviewRequestDto changeReviewRequestDto){

        String reviewText = changeReviewRequestDto.reviewText();
        Double memberRating = changeReviewRequestDto.memberRating();
        String  memberRatingGenre = changeReviewRequestDto.memberRatingGenre();

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);
        review.changeReview(reviewText, memberRating, memberRatingGenre);

        reviewRepository.save(review);
        return review.getReviewId();
    }

    /*
    내가 쓴 리뷰 삭제
     */
    public void delete(Long reviewId){

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        reviewRepository.delete(review);
    }

    /*
    내가 쓴 리뷰 조회
     */
    public List<ReviewResponseDto> findReviewListByMemberId(Long memberId, int page){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("reviewId")); // 리뷰 ID 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));

        List<Review> reviewList = reviewRepository.searchByMember(memberId, pageable);
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
    public List<ReviewResponseDto> findReviewListByMovieId(Long movieId, int page){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("reviewId")); // 리뷰 ID 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));

        List<Review> reviewList = reviewRepository.searchByMovie(movieId, pageable);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

}
