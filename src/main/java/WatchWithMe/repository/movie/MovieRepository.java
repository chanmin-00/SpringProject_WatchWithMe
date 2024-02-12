package WatchWithMe.repository.movie;

import WatchWithMe.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    // 영화명 and 개봉 연도 and 장르 기준 조회
    List<Movie> findByTitleAndOpenYearAndGenre(String title, String openYear, String genre);

    // 장르 기준 조회
    List<Movie> findByGenre(String genre);

    // 영화 전체 조회, 페이징 처리 메소드
    Page<Movie> findAll(Pageable pageable);
}
