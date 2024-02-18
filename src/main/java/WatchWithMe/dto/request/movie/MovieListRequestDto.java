package WatchWithMe.dto.request.movie;

import jakarta.annotation.Nullable;

public record MovieListRequestDto (
        @Nullable
        String titleOrActorOrDirector, // 영화명 또는 감독명 또는 배우명
        @Nullable
        String openYear, // 개봉 연도
        @Nullable
        Integer userRatingHigh, // 최고 평점
        @Nullable
        Integer userRatingLow, // 최저 평점
        @Nullable
        String genre // 장르
) {}
