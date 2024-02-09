package WatchWithMe.dto.response;

import WatchWithMe.domain.Director;
import lombok.Getter;

@Getter
public class DirectorResponseDto {
    private final String name;

    public DirectorResponseDto(Director director){
        this.name = director.getName();
    }
}
