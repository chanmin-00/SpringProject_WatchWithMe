package WatchWithMe.redis.service;

import WatchWithMe.redis.repository.ReviewCacheRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCacheServiceImpl implements ReviewCacheService{

    private final ReviewCacheRepository reviewCacheRepository;

    @Override
    @Transactional
    public void saveRating(String movieId, String value) {
        reviewCacheRepository.saveRating(movieId, value);
        reviewCacheRepository.saveChangedMovie(movieId);
    }

    @Override
    public List<String> getRatingList(String movieId) {
        return reviewCacheRepository.getRatingList(movieId);
    }

    @Override
    public Set<String> getChangedMovieList() {
        return reviewCacheRepository.getChangedMovieList();
    }

    @Override
    @Transactional
    public void deleteRatingList(String movieId) {
        reviewCacheRepository.deleteRatingList(movieId);
    }

    @Override
    @Transactional
    public void deleteChangedMovie() {
        reviewCacheRepository.deleteChangedMovie();
    }


}
