package WatchWithMe.dto.response;

import WatchWithMe.domain.MovieActor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieActorResponseDto {
    private ActorResponseDto actorResponseDto;

    public MovieActorResponseDto(MovieActor movieActor){
        this.actorResponseDto = new ActorResponseDto(movieActor.getActor());
    }
}
