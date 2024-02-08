package WatchWithMe.repository.movie;

import WatchWithMe.domain.Movie;
import WatchWithMe.dto.request.movie.MovieListRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> search(MovieListRequestDto movieListRequestDto, Pageable pageable);
}
