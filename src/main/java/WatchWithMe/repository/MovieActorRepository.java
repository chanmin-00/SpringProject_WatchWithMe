package WatchWithMe.repository;

import WatchWithMe.domain.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieActorRepository extends JpaRepository<MovieActor, Long> {
}
