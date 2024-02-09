package WatchWithMe.dto.response;

import WatchWithMe.domain.Actor;
import lombok.Getter;

@Getter
public class ActorResponseDto {
    private final String name;

    public ActorResponseDto(Actor actor){
        this.name = actor.getName();
    }
}
