package WatchWithMe.repository.actor;

import WatchWithMe.domain.Actor;
import WatchWithMe.dto.request.ActorListRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActorRepositoryCustom {
    List<Actor> search(ActorListRequestDto actorListRequestDto, Pageable pageable);
}
