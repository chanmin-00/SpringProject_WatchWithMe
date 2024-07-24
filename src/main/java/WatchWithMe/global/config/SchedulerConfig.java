package WatchWithMe.global.config;

import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig implements SchedulingConfigurer {

    private final int POOL_SIZE = 10; // 스레드 풀 사이즈
    private final MovieService movieService;

    @Scheduled(initialDelay=1000, fixedDelay=60000 * 5) // 5분 마다 수행
    public void updateMovieList() {
        try {
            movieService.updateMovieList();
            log.info("영화 정보 업데이트 완료");
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._INTERNAL_SERVER_ERROR);
        }
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
