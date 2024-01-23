package WatchWithMe.repository.actor;

import WatchWithMe.domain.Actor;
import WatchWithMe.dto.request.ActorListRequestDto;
import java.util.List;

public interface ActorRepositoryCustom {
    List<Actor> search(ActorListRequestDto actorListRequestDto);
}
