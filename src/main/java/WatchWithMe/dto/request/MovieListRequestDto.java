package WatchWithMe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class MovieListRequestDto {

    private String title; // 영화 제목

    private String openYear; // 제작 연도

    private Integer userRating; // 평균 평점, 1-5 정수

    private String genre; // 장르
}
