package WatchWithMe.dto.request.movie;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MovieListRequestDto {

    @NotBlank(message = "영화명을 입력해주세요")
    private String title; // 영화명

    @NotBlank(message = "개봉 연도를 입력해주세요")
    private String openYear; // 개봉 연도

    @NotBlank(message = "최고 평점을 입력해주세요")
    private Integer userRatingHigh; // 최고 평점

    @NotBlank(message = "최저 평점을 입력해주세요")
    private Integer userRatingLow; // 최저 평점

    @NotBlank(message = "장르를 입력해주세요")
    private String genre; // 장르
}
