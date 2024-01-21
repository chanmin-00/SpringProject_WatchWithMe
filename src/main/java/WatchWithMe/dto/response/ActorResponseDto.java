package WatchWithMe.dto.response;

import WatchWithMe.domain.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorResponseDto {
    private String name;

    public ActorResponseDto(Actor actor){
        this.name = actor.getName();
    }
}
