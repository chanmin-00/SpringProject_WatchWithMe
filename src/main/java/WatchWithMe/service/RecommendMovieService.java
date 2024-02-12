package WatchWithMe.service;

import WatchWithMe.domain.*;
import WatchWithMe.dto.response.movie.MovieResponseDto;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.repository.actor.ActorRepository;
import WatchWithMe.repository.director.DirectorRepository;
import WatchWithMe.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendMovieService {

    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;

    // 사용자 기반 영화 추천 서비스
    public List<MovieResponseDto> recommendMovie(Long memberId) {
        List<MovieResponseDto> movieResponseDtoList = new ArrayList<>(); // 리턴값
        Map<Movie, Long> recommendPriorityMap = new HashMap<>(); // 영화 추천 우선 순위 Map

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        List<String> favoriteGenre = member.getFavoriteGenre(); // 사용자 선호 장르
        Collections.reverse(favoriteGenre); // 역순, index 가 클수록 사용자가 원하는 장르
        List<String> favoriteActor = member.getFavoriteActor(); // 사용자 선호 배우
        List<String> favoriteDirector = member.getFavoriteDirector(); // 사용자 선호 감독

        for(int i = 0;i < favoriteGenre.size();i++) { // 선호 장르에 해당하는 각 영화 개수 세기
            List<Movie> movieList = movieRepository.findByGenre(favoriteGenre.get(i));

            for(int j = 0;j < movieList.size();j++) {
                Movie movie = movieList.get(j);
                Double userRating = movie.getUserRating();
                if (userRating == null)
                    userRating = 0.0;

                long priorityNumber = i + 1L; // 평점값과 선호 장르 순위에 따라 우선 순위 다르게 부여
                if (userRating > 4) priorityNumber += 5L;
                else if (userRating > 3) priorityNumber += 4L;
                else if (userRating > 2) priorityNumber += 3L;
                else if (userRating > 1) priorityNumber += 2L;
                else priorityNumber += 1L;

                long finalPriorityNumber = priorityNumber;
                recommendPriorityMap.compute(movie, (key, value) -> (value == null) ? finalPriorityNumber : value + 1L);
            }
        }

        for(int i = 0;i < favoriteActor.size();i++) { // 선호 배우에 해당하는 각 영화 개수 세기
            Actor actor = actorRepository.findByName(favoriteActor.get(i)).orElse(null);
            if (actor == null)
                throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

            List<MovieActor> movieActorList = actor.getMovieActorList();
            for (int j = 0;j < movieActorList.size();j++) {
                Movie movie = movieActorList.get(j).getMovie();
                Double userRating = movie.getUserRating();
                if (userRating == null)
                    userRating = 0.0;

                long priorityNumber = 1L; // 평점값에 따라 우선 순위 다르게 부여
                if (userRating > 4) priorityNumber += 5L;
                else if (userRating > 3) priorityNumber += 4L;
                else if (userRating > 2) priorityNumber += 3L;
                else if (userRating > 1) priorityNumber += 2L;
                else priorityNumber += 1L;

                long finalPriorityNumber = priorityNumber;
                recommendPriorityMap.compute(movie, (key, value) -> (value == null) ? finalPriorityNumber : value + 1L);
            }
        }

        for(int i = 0;i < favoriteDirector.size();i++) { // 선호 감독에 해당하는 각 영화 개수 세기
            Director director = directorRepository.findByName(favoriteDirector.get(i)).orElse(null);
            if (director == null)
                throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

            List<MovieDirector> movieDirectorList = director.getMovieDirectorList();
            for (int j = 0;j < movieDirectorList.size();j++) {
                Movie movie = movieDirectorList.get(j).getMovie();
                Double userRating = movie.getUserRating();
                if (userRating == null)
                    userRating = 0.0;

                long priorityNumber = 1L; // 평점값에 따라 우선 순위 다르게 부여
                if (userRating > 4) priorityNumber += 5L;
                else if (userRating > 3) priorityNumber += 4L;
                else if (userRating > 2) priorityNumber += 3L;
                else if (userRating > 1) priorityNumber += 2L;
                else priorityNumber += 1L;

                long finalPriorityNumber = priorityNumber;
                recommendPriorityMap.compute(movie, (key, value) -> (value == null) ? finalPriorityNumber : value + 1L);
            }
        }

        // 우선 순위가 높은 영화 추천 리스트 추출
        List<Movie> recommendMovieList = recommendPriorityMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .map(Map.Entry::getKey)
                .toList();

        for(int i = 0; i < recommendMovieList.size(); i++){
            MovieResponseDto movieResponseDto = new MovieResponseDto(recommendMovieList.get(i));
            movieResponseDtoList.add(movieResponseDto);
        }

        return movieResponseDtoList;
    }

}
