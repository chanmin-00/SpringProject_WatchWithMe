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
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    // 리뷰 DB 저장
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

        Double userRating = movie.getUserRating(); // 영화 평균 평점
        int reviewListSize = movie.getReviewList().size(); // 영화별 리뷰 개수
        if (userRating == null)
            userRating = 0.0;

        userRating = (userRating * reviewListSize + memberRating) / (reviewListSize + 1);
        movie.setUserRating(userRating); // 영화 평균 평점 갱신

        Review review = Review.createReview(reviewText, memberRating, memberRatingGenre);
        review.setMember(member);
        review.setMovie(movie);
        reviewRepository.save(review);

        return review.getReviewId();
    }

    // 리뷰 수정
    public Long change(Long reviewId, ChangeReviewRequestDto changeReviewRequestDto){

        String reviewText = changeReviewRequestDto.reviewText();
        Double memberRating = changeReviewRequestDto.memberRating();
        String  memberRatingGenre = changeReviewRequestDto.memberRatingGenre();

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Movie movie = movieRepository.findById(review.getMovie().getMovieId()).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Double prevMemberRating = review.getMemberRating(); // 수정되기 전 평점
        Double userRating = movie.getUserRating(); // 영화 평균 평점
        int reviewListSize = movie.getReviewList().size(); // 영화별 리뷰 개수
        if (userRating == null)
            userRating = 0.0;

        userRating = (userRating * reviewListSize - prevMemberRating +  memberRating) / reviewListSize;
        movie.setUserRating(userRating); // 영화 평균 평점 갱신

        review.changeReview(reviewText, memberRating, memberRatingGenre);

        reviewRepository.save(review);
        return review.getReviewId();
    }

    // 내가 쓴 리뷰 삭제
    public void delete(Long reviewId){

        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Movie movie = movieRepository.findById(review.getMovie().getMovieId()).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        Double prevMemberRating = review.getMemberRating(); // 삭제 리뷰 평점
        Double userRating = movie.getUserRating(); // 영화 평균 평점
        int reviewListSize = movie.getReviewList().size(); // 영화별 리뷰 개수
        if (userRating == null)
            userRating = 0.0;

        userRating = (userRating * reviewListSize - prevMemberRating) / (reviewListSize - 1);
        movie.setUserRating(userRating); // 영화 평균 평점 갱신

        reviewRepository.delete(review);
    }

    // 내가 쓴 리뷰 조회
    public List<ReviewResponseDto> findReviewListByMemberId(Long memberId, int page){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.searchByMember(memberId, pageable);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회
    public List<ReviewResponseDto> findReviewListByMovieId(Long movieId, int page){

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.searchByMovie(movieId, pageable);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회, 평점 높음 순
    public List<ReviewResponseDto> findReviewListByMovieIdRatingDesc(Long movieId, int page) {

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.searchByMovieRatingDesc(movieId, pageable);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회, 평점 낮음 순
    public List<ReviewResponseDto> findReviewListByMovieIdRatingAsc(Long movieId, int page) {

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.searchByMovieRatingAsc(movieId, pageable);
        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }
}
