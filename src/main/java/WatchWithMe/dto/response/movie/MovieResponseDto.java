package WatchWithMe.dto.response.movie;

import WatchWithMe.domain.Movie;
import WatchWithMe.dto.response.MovieActorResponseDto;
import WatchWithMe.dto.response.MovieDirectorResponseDto;
import lombok.Getter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MovieResponseDto {

    private final Long movieId; // movieId

    private final String title; // 영화명

    private final String openYear; // 개봉 연도

    private final Double userRating; // 평점

    private final String genre; // 장르

    private final List<MovieActorResponseDto> movieActorDtoList; // 영화 배우 리스트
    private final List<MovieDirectorResponseDto> movieDirectorDtoList; // 영화 배우 리스트

    public MovieResponseDto(Movie movie) {
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

    // 평점 비교
    public static Comparator<MovieResponseDto> userRatingComparator
            = Comparator.comparingDouble(
            movieResponseDto -> movieResponseDto.getUserRating() != null ?
                    movieResponseDto.getUserRating() : Double.MIN_VALUE
    );

}
