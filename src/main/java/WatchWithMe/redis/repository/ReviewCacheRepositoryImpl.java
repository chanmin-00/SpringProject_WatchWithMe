package WatchWithMe.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReviewCacheRepositoryImpl implements ReviewCacheRepository{

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REVIEW_KEY_PREFIX = "reviews : ";
    private static final String CHANGED_MOVIES_KEY = "changed_movies";

    @Override
    public void saveRating(String movieId, String value) {
        redisTemplate.opsForList().rightPush(REVIEW_KEY_PREFIX + movieId, value);
    }

    @Override
    public void saveChangedMovie(String movieId) {
        redisTemplate.opsForSet().add(CHANGED_MOVIES_KEY, movieId);
    }

    @Override
    public List<String> getRatingList(String movieId) {
        return redisTemplate.opsForList().range(REVIEW_KEY_PREFIX + movieId, 0, -1);
    }

    @Override
    public Set<String> getChangedMovieList() {
        return redisTemplate.opsForSet().members(CHANGED_MOVIES_KEY);
    }

    @Override
    public void deleteRatingList(String movieId) {
        redisTemplate.delete(REVIEW_KEY_PREFIX + movieId);
    }

    @Override
    public void deleteChangedMovie() {
        redisTemplate.delete(CHANGED_MOVIES_KEY);
    }

}
