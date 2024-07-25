package WatchWithMe.redis.service;

import java.util.List;
import java.util.Set;

public interface ReviewCacheService {

    void saveRating(String movieId, String value);

    List<String> getRatingList(String movieId);

    Set<String> getChangedMovieList();

    void deleteRatingList(String movieId);

    void deleteChangedMovie();
}
