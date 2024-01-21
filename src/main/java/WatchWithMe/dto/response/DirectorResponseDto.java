package WatchWithMe.dto.response;

import WatchWithMe.domain.Director;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectorResponseDto {
    private String name;

    public DirectorResponseDto(Director director){
        this.name = director.getName();
    }
}
