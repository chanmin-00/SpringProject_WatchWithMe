package WatchWithMe.dto.response;

import WatchWithMe.domain.MovieActor;
import lombok.Getter;

@Getter
public class MovieActorResponseDto {
    private final ActorResponseDto actorResponseDto;

    public MovieActorResponseDto(MovieActor movieActor){
        this.actorResponseDto = new ActorResponseDto(movieActor.getActor());
    }
}
