package WatchWithMe.service;

import WatchWithMe.domain.*;
import WatchWithMe.dto.request.review.ChangeReviewRequestDto;
import WatchWithMe.dto.request.review.SearchReviewRequestDto;
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
import java.util.*;

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

        Member member = review.getMember();
        if (member == null)
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

        Member member = review.getMember();
        if (member == null)
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

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.findByMember(memberId, pageable);
        if (reviewList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }
        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회
    public List<ReviewResponseDto> findReviewListByMovieId(Long movieId, int page){

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.findByMovie(movieId, pageable);
        if (reviewList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회, 평점 높음 순
    public List<ReviewResponseDto> findReviewListByMovieIdRatingDesc(Long movieId, int page) {

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.findByMovieRatingDesc(movieId, pageable);
        if (reviewList.isEmpty())
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 영화 리뷰 조회, 평점 낮음 순
    public List<ReviewResponseDto> findReviewListByMovieIdRatingAsc(Long movieId, int page) {

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._BAD_REQUEST);

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        page = page - 1; // page, 0부터 시작
        Pageable pageable = PageRequest.of(page, 10);

        List<Review> reviewList = reviewRepository.findByMovieRatingAsc(movieId, pageable);
        if (reviewList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        for(int i = 0;i < reviewList.size();i++) {
            Review review = reviewList.get(i);
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return reviewResponseDtoList;
    }

    // 리뷰 조건 검색, 리뷰 텍스트 검색
    public List<ReviewResponseDto> searchReviewText(SearchReviewRequestDto searchReviewRequestDto, int page){

        String searchText = searchReviewRequestDto.searchText();

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>(); // 리턴값 리스트

        List<String> searchTextCombinationList = new ArrayList<>(); // 검색어 조합 단어 리스트
        String[] words = searchText.split("[\\s,]+"); // 검색어를 ","와 공백으로 구분
        for (String word : words) {
            if (word.length() >= 2) {
                for (int i = 0; i < word.length() - 1; i++) {
                    for (int j = i + 1; j <= word.length(); j++) {
                        searchTextCombinationList.add(word.substring(i, j));
                    }
                }
            }
        }

        searchTextCombinationList.sort(Comparator.comparing(String::length).reversed()); // 단어 길이순, 내림 차순 정렬
        Map<Review, Long> reviewMap = new HashMap<>(); // 리뷰 중복 방지용 map
        for (String word : searchTextCombinationList) {
            List<Review> reviewList = reviewRepository.searchReviewText(word);

            for (Review review : reviewList) {
                if (!reviewMap.containsKey(review)) {
                    reviewMap.put(review, 1L);
                    ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
                    reviewResponseDtoList.add(reviewResponseDto);
                }
            }
        }

        if (reviewResponseDtoList.isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        int pageSize = 10; // 페이지 요소 개수
        int startIndex = (page - 1) * pageSize; // 페이징 시작 인덱스
        int endIndex = Math.min(startIndex + pageSize, reviewResponseDtoList.size()); // 페이징 끝 인덱스

        return reviewResponseDtoList.subList(startIndex, endIndex); // 페이징 처리 후 리턴
    }
}
