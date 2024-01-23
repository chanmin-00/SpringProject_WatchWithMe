package WatchWithMe.repository.actor;

import WatchWithMe.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> , ActorRepositoryCustom {
    Optional<Actor> findByName(String name);
}
