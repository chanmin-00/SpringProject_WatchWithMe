package WatchWithMe.dto.response;

import WatchWithMe.domain.Movie;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MovieListResponseDto {

    private Long movieId; // movieId

    private String title; // 영화명

    private String openYear; // 개봉 연도

    private Double userRating; // 평점

    private String genre; // 장르

    private List<MovieActorResponseDto> movieActorDtoList; // 영화 배우 리스트
    private List<MovieDirectorResponseDto> movieDirectorDtoList; // 영화 배우 리스트

    public MovieListResponseDto(Movie movie) {
        this.movieId = movie.getMovieId();
        this.title = movie.getTitle();
        this.openYear = movie.getOpenYear();
        this.userRating = movie.getUserRating();
        this.genre = movie.getGenre();
        this.movieActorDtoList = movie.getMovieActorList().stream()
                .map(MovieActorResponseDto::new)
                .collect(Collectors.toList());
        this.movieDirectorDtoList = movie.getMovieDirectorList().stream()
                .map(MovieDirectorResponseDto::new)
                .collect(Collectors.toList());
    }
}
