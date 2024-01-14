package WatchWithMe.service;

import WatchWithMe.domain.Actor;
import WatchWithMe.domain.Movie;
import WatchWithMe.domain.MovieActor;
import WatchWithMe.repository.ActorRepository;
import WatchWithMe.repository.MovieRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    //영화 정보 update
    public void updateMovieList(String targetDate) throws ParseException {

        String boxOfficeSiteUrl; // 영화 boxOffice site 주소
        String movieInfoSiteUrl; // 영화 상세 정보 site 주소
        String key; // 오픈 API 이용 key 값
        String itemPerPage; // 결과 행 수
        String movieGenre; // 영화 장르
        String movieName; // 영화명
        String movieOpenDate; // 영화 개봉 연도
        String actorName; // 배우명
        List<String> movieCodeList = new ArrayList<>();

        boxOfficeSiteUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice";
        movieInfoSiteUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie";
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
            movieOpenDate = movieInfo.get("openDt").toString(); // 영화 개봉 연도 설정
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

            for (int j = 0; j < actorList.size(); j++) {
                JSONObject object;
                object = (JSONObject) actorList.get(j);
                actorName = object.get("peopleNm").toString(); // 영화 배우 이름 설정

                if (!actorRepository.findByName(actorName).isEmpty()){
                    continue; // DB에 이미 존재하는 경우
                }
                Actor actor = Actor.createActor(actorName); // 배우 객체 생성
                MovieActor movieActor = MovieActor.createMovieActor(movie, actor);
                actor.addMovieActor(movieActor);
                movie.addMovieActor(movieActor);
                actorRepository.save(actor);
            }
            movieRepository.save(movie);
        }
    }
}
