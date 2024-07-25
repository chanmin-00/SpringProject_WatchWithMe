package WatchWithMe.redis.repository;

import java.util.List;
import java.util.Set;

public interface ReviewCacheRepository {

    void saveRating(String movieId, String value);

    void saveChangedMovie(String movieId);

    List<String> getRatingList(String movieId);

    Set<String> getChangedMovieList();

    void deleteRatingList(String movieId);

    void deleteChangedMovie();


}
