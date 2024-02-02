package WatchWithMe.repository.movie;

import WatchWithMe.domain.Movie;
import WatchWithMe.dto.request.movie.MovieListRequestDto;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> search(MovieListRequestDto movieListRequestDto);
}
