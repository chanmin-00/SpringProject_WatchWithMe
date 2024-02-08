package WatchWithMe.dto.request.movie;

import jakarta.validation.constraints.NotBlank;

public record MovieListRequestDto (
        @NotBlank(message = "영화명을 입력해주세요")
        String title, // 영화명

        @NotBlank(message = "개봉 연도를 입력해주세요")
        String openYear, // 개봉 연도

        @NotBlank(message = "최고 평점을 입력해주세요")
        Integer userRatingHigh, // 최고 평점

        @NotBlank(message = "최저 평점을 입력해주세요")
        Integer userRatingLow, // 최저 평점

        @NotBlank(message = "장르를 입력해주세요")
        String genre // 장르
) {}
