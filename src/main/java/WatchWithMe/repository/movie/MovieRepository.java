package WatchWithMe.repository.movie;

import WatchWithMe.domain.Movie;
import WatchWithMe.repository.movie.MovieRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    // 영화명 and 개봉 연도 and 장르 기준 조회
    List<Movie> findByTitleAndOpenYearAndGenre(String title, String openYear, String genre);
}
