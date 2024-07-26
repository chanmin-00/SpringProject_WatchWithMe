package WatchWithMe.global.config;

import WatchWithMe.domain.Movie;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.redis.service.ReviewCacheService;
import WatchWithMe.service.MovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig implements SchedulingConfigurer {

    private final int POOL_SIZE = 10; // 스레드 풀 사이즈
    private final MovieService movieService;
    private final ReviewCacheService reviewCacheService;

    @Scheduled(initialDelay=1000, fixedDelay =  1000 * 60 * 60 * 24) // 24시간마다 실행
    public void updateMovieList() {
        try {
            movieService.updateMovieList();
            log.info("영화 정보 업데이트 완료");
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._INTERNAL_SERVER_ERROR);
        }
    }

    @Scheduled(initialDelay=1000, fixedDelay = 1000 * 60 * 60 *24 ) // 24시간마다 실행
    @Transactional
    public void calculateAverageReviewRating() {
        Set<String> changedMovieList = reviewCacheService.getChangedMovieList();
        if (changedMovieList != null) {
            for (String movieIdStr : changedMovieList) {
                Long movieId = Long.parseLong(movieIdStr);
                List<String> ratingList = reviewCacheService.getRatingList(movieIdStr);
                if (ratingList != null && !ratingList.isEmpty()){
                    Movie movie = movieService.getMovie(movieId);
                    double currentRating = (movie.getUserRating() != null) ? movie.getUserRating() : 0.0;
                    int reviewCount = movieService.getReviewList(movieId).size();

                    double newSum = ratingList.stream()
                                           .mapToDouble(Double::parseDouble)
                                           .sum() + currentRating * (reviewCount - ratingList.size());

                    movie.setUserRating(newSum / reviewCount);
                    reviewCacheService.deleteRatingList(movieIdStr);
                }
            }
            reviewCacheService.deleteChangedMovie();
        }
        log.info("평점 업데이트 완료");
    }


    // 여러 개의 스레드 동시 처리
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("test-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
