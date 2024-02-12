package WatchWithMe.service;

import WatchWithMe.domain.*;
import WatchWithMe.dto.request.movie.MovieListRequestDto;
import WatchWithMe.dto.response.movie.MoviePageResponseDto;
import WatchWithMe.dto.response.movie.MovieResponseDto;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.actor.ActorRepository;
import WatchWithMe.repository.director.DirectorRepository;
import WatchWithMe.repository.MovieActorRepository;
import WatchWithMe.repository.MovieDirectorRepository;
import WatchWithMe.repository.movie.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ActorRepository actorRepository;
    private final MovieActorRepository movieActorRepository;
    private final MovieDirectorRepository movieDirectorRepository;
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    //영화 정보 update
    public void updateMovieList() throws Exception {

        String boxOfficeSiteUrl; // 영화 boxOffice site 주소
        String movieInfoSiteUrl; // 영화 상세 정보 site 주소
        String targetDate; // 조회 날짜
        String key; // 오픈 API 이용 key 값
        String itemPerPage; // 결과 행 수
        String movieGenre; // 영화 장르
        String movieName; // 영화명
        String movieOpenDate; // 영화 개봉 연도
        String actorName; // 배우명
        String directorName; // 감독명
        List<String> movieCodeList = new ArrayList<>();

        boxOfficeSiteUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice";
        movieInfoSiteUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie";
        targetDate = LocalDate.now().minusDays(1).toString(); // 어제 날짜
        targetDate = targetDate.replace("-","");
        key = "05729aeda1ecc537be73bd2cc911b528";
        itemPerPage = "10";

        // 오픈 API를 통해 JSON 데이터 요청
        URI uri = UriComponentsBuilder.fromUriString(boxOfficeSiteUrl)
                .path("/searchDailyBoxOfficeList.json")
                .queryParam("key", key)
                .queryParam("itemPerPage", itemPerPage)
                .queryParam("targetDt", targetDate)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // JSON 문자열 파싱
        JSONParser jsonParser = new JSONParser();
        JSONObject responseBody = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject boxOfficeResult = (JSONObject) responseBody.get("boxOfficeResult");
        JSONArray dailyBoxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");

        for (int i = 0; i < dailyBoxOfficeList.size(); i++) { // boxOffice 10위권 영화 코드 저장
            JSONObject movieCode;
            movieCode = (JSONObject) dailyBoxOfficeList.get(i);
            movieCodeList.add(movieCode.get("movieCd").toString());
        }

        for (int i = 0; i < movieCodeList.size(); i++) { // 영화 코드별 API 요청에 따른 영화 상세 정보 추출

            // 오픈 API를 통해 JSON 데이터 요청
            uri = UriComponentsBuilder.fromUriString(movieInfoSiteUrl)
                    .path("/searchMovieInfo.json")
                    .queryParam("key", key)
                    .queryParam("movieCd", movieCodeList.get(i))
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUri();
            restTemplate = new RestTemplate();
            request = RequestEntity.get(uri).build();
            response = restTemplate.exchange(request, String.class);

            // JSON 문자열 파싱
            jsonParser = new JSONParser();
            responseBody = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject movieInfoResult = (JSONObject) responseBody.get("movieInfoResult");
            JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");
            JSONArray genreList = (JSONArray) movieInfo.get("genres");
            JSONArray actorList = (JSONArray) movieInfo.get("actors");
            JSONArray directorList = (JSONArray) movieInfo.get("directors");

            movieName = movieInfo.get("movieNm").toString(); // 영화명 설정
            movieOpenDate = movieInfo.get("openDt").toString().substring(0,4); // 영화 개봉 연도 설정
            movieGenre = "";
            for (int j = 0; j < genreList.size(); j++) {
                JSONObject object;
                object = (JSONObject) genreList.get(j);
                movieGenre = object.get("genreNm").toString(); // 영화 장르 설정
                break; // 대표 장르 1개만 설정
            }

            if (!movieRepository.findByTitleAndOpenYearAndGenre(movieName, movieOpenDate, movieGenre).isEmpty()){
                continue; // DB에 이미 존재하는 경우
            }

            Movie movie = Movie.createMovie(movieName, movieOpenDate, movieGenre);
            movieRepository.save(movie);
            for (int j = 0; j < actorList.size(); j++) {
                Actor actor;
                JSONObject object;
                object = (JSONObject) actorList.get(j);
                actorName = object.get("peopleNm").toString(); // 영화 배우 이름 설정

                actor = actorRepository.findByName(actorName).orElse(null);
                if (actor == null){ // DB에 존재하지 않는 경우
                    actor = Actor.createActor(actorName); // 배우 객체 생성
                }
                MovieActor movieActor = MovieActor.createMovieActor(movie, actor);
                actor.addMovieActor(movieActor);
                movie.addMovieActor(movieActor);
                actorRepository.save(actor);
                movieActorRepository.save(movieActor);
            }

            for (int j = 0; j < directorList.size(); j++) {
                Director director;
                JSONObject object;
                object = (JSONObject) directorList.get(j);
                directorName = object.get("peopleNm").toString(); // 영화 감독 이름 설정

                director = directorRepository.findByName(directorName).orElse(null);
                if (director == null){ // DB에 존재하지 않는 경우
                    director = director.createDirector(directorName); // 감독 객체 생성
                }
                MovieDirector movieDirector = MovieDirector.createMovieDirector(movie, director);
                director.addMovieDirector(movieDirector);
                movie.addMovieDirector(movieDirector);
                directorRepository.save(director);
                movieDirectorRepository.save(movieDirector);
            }

        }
    }

    // 영화 단건(id) 조회
    public MovieResponseDto getMovieById(Long movieId) {

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        return new MovieResponseDto(movie);
    }

    // 영화 목록 전체 조회, 페이징 기능 구현
    public MoviePageResponseDto getMovieList(int page) {

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("createdAt")); // 최신 영화 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        Page<Movie> moviePage = movieRepository.findAll(pageable); // 조건에 따른 페이지 조회

        if (moviePage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS); // 조회 내용이 없는 경우

        return new MoviePageResponseDto(moviePage);
    }

    // 영화 목록 전체 조회, 평점 높음 순 페이징 기능 구현
    public MoviePageResponseDto getUserRatingDescMovieList(int page) {

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("userRating")); // 상위 평점 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        Page<Movie> moviePage = movieRepository.findAll(pageable); // 조건에 따른 페이지 조회

        if (moviePage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS); // 조회 내용이 없는 경우

        return new MoviePageResponseDto(moviePage);
    }

    // 영화 목록 전체 조회, 평점 낮음 순 페이징 기능 구현
    public MoviePageResponseDto getUserRatingAscMovieList(int page) {

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.asc("userRating")); // 하위 평점 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));
        Page<Movie> moviePage = movieRepository.findAll(pageable); // 조건에 따른 페이지 조회

        if (moviePage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS); // 조회 내용이 없는 경우

        return new MoviePageResponseDto(moviePage);
    }

    // 영화 목록 전체 조회, 리뷰 많음 순, 페이징 기능 구현
    public MoviePageResponseDto getReviewMostMovieList(int page) {

        page = page - 1; // page, 0부터 시작

        Pageable pageable = PageRequest.of(page, 10);
        Page<Movie> moviePage = movieRepository.findAllMostReview(pageable); // 조건에 따른 페이지 조회

        if (moviePage.getContent().isEmpty())
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS); // 조회 내용이 없는 경우

        return new MoviePageResponseDto(moviePage);
    }

    // 영화 조건 검색 (영화명, 영화 장르, 개봉 연도, 평점)
    public List<MovieResponseDto> searchMovieList(MovieListRequestDto movieListRequestDto, int page){
        List<MovieResponseDto> movieResponseDtoList = new ArrayList<>();

        List<Sort.Order> sort = new ArrayList<>();
        page = page - 1; // page, 0부터 시작

        sort.add(Sort.Order.desc("createdAt")); // 최신 영화 기준 정렬 조건 추가
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort));

        List<Movie> movieList = movieRepository.search(movieListRequestDto, pageable);
        for(int i = 0; i < movieList.size(); i++){
            MovieResponseDto movieResponseDto = new MovieResponseDto(movieList.get(i));
            movieResponseDtoList.add(movieResponseDto);
        }
        return movieResponseDtoList;
    }
}
