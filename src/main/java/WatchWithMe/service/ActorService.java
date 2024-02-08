package WatchWithMe.service;

import WatchWithMe.domain.Actor;
import WatchWithMe.domain.Movie;
import WatchWithMe.domain.MovieActor;
import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.dto.response.movie.MovieResponseDto;
import WatchWithMe.repository.actor.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    // 배우별 영화 조회
    public List<MovieResponseDto> searchMovieListByActor(ActorListRequestDto actorListRequestDto, int page){
        List<MovieResponseDto> movieListResponseDtoList = new ArrayList<>();

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("actorId")); // 최신 영화 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));

        List<Actor> actorList = actorRepository.search(actorListRequestDto, pageable);
        for(int i = 0;i < actorList.size();i++){
            Actor actor = actorList.get(i);
            List<MovieActor> movieActorList = actor.getMovieActorList();
            for(int j = 0;j < movieActorList.size();j++){
                MovieActor movieActor = movieActorList.get(j);
                Movie movie = movieActor.getMovie();
                MovieResponseDto movieListResponseDto = new MovieResponseDto(movie);
                movieListResponseDtoList.add(movieListResponseDto);
            }
        }
        return movieListResponseDtoList;
    }
}
