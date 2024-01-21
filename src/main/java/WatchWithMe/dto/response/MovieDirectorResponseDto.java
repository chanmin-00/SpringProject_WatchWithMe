package WatchWithMe.dto.response;

import WatchWithMe.domain.MovieDirector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDirectorResponseDto {
    private DirectorResponseDto directorResponseDto;

    public MovieDirectorResponseDto(MovieDirector movieDirector){
        this.directorResponseDto = new DirectorResponseDto(movieDirector.getDirector());
    }
}
