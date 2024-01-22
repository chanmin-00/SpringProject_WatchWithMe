package WatchWithMe.repository.director;

import WatchWithMe.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long>, DirectorRepositoryCustom {
    Optional<Director> findByName(String name);
}
