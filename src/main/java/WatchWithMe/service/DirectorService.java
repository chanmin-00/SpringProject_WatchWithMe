package WatchWithMe.service;

import WatchWithMe.domain.*;
import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.dto.response.movie.MovieResponseDto;
import WatchWithMe.repository.director.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    // 감독별 영화 조회
    public List<MovieResponseDto> searchMovieListByDirector(DirectorListRequestDto directorListRequestDto){
        List<MovieResponseDto> movieListResponseDtoList = new ArrayList<>();

        List<Director> directorList = directorRepository.search(directorListRequestDto);
        for(int i = 0;i < directorList.size();i++){
            Director director = directorList.get(i);
            List<MovieDirector> movieDirectorList = director.getMovieDirectorList();
            for(int j = 0;j < movieDirectorList.size();j++){
                MovieDirector movieDirector = movieDirectorList.get(j);
                Movie movie = movieDirector.getMovie();
                MovieResponseDto movieListResponseDto = new MovieResponseDto(movie);
                movieListResponseDtoList.add(movieListResponseDto);
            }
        }
        return movieListResponseDtoList;
    }
}
