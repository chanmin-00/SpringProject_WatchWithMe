package WatchWithMe.dto.response;

import WatchWithMe.domain.MovieDirector;
import lombok.Getter;

@Getter
public class MovieDirectorResponseDto {
    private final DirectorResponseDto directorResponseDto;

    public MovieDirectorResponseDto(MovieDirector movieDirector){
        this.directorResponseDto = new DirectorResponseDto(movieDirector.getDirector());
    }
}
