package WatchWithMe.service;

import WatchWithMe.domain.Actor;
import WatchWithMe.domain.Movie;
import WatchWithMe.domain.MovieActor;
import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.dto.response.MovieListResponseDto;
import WatchWithMe.repository.actor.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    // 배우별 영화 조회
    public List<MovieListResponseDto> searchMovieListByActor(ActorListRequestDto actorListRequestDto){
        List<MovieListResponseDto> movieListResponseDtoList = new ArrayList<>();

        List<Actor> actorList = actorRepository.search(actorListRequestDto);
        for(int i = 0;i < actorList.size();i++){
            Actor actor = actorList.get(i);
            List<MovieActor> movieActorList = actor.getMovieActorList();
            for(int j = 0;j < movieActorList.size();j++){
                MovieActor movieActor = movieActorList.get(j);
                Movie movie = movieActor.getMovie();
                MovieListResponseDto movieListResponseDto = new MovieListResponseDto(movie);
                movieListResponseDtoList.add(movieListResponseDto);
            }
        }
        return movieListResponseDtoList;
    }
}
