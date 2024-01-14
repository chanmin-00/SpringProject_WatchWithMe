package WatchWithMe.repository;

import WatchWithMe.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 영화명 and 개봉 연도 and 장르 기준 조회
    List<Movie> findByTitleAndOpenYearAndGenre(String title, String openYear, String genre);
}
