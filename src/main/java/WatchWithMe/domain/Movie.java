package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Movie extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    private String title; // 영화 제목

    private String openYear; // 제작 연도

    private Double userRating; // 평균 평점

    private String genre; // 장르

    @OneToMany(mappedBy = "movie")
    private List<Review> reviewList = new ArrayList<>(); // 리뷰 목록

    @OneToMany(mappedBy = "movie")
    private List<MovieActor> movieActorList = new ArrayList<>(); // 출연 배우 목록

    @OneToMany(mappedBy = "movie")
    private List<MovieDirector> movieDirectorList = new ArrayList<>(); // 출연 감독 목록

    // 생성 메서드
    public static Movie createMovie(String title, String year, String genre){
        Movie movie = new Movie();
        movie.title = title;
        movie.openYear = year;
        movie.userRating = null;
        movie.genre = genre;
        return movie;
    }

    public void setUserRating(Double userRating) { this.userRating = userRating; }

    public void addMovieActor(MovieActor movieActor) {
        this.movieActorList.add(movieActor);
    }

    public void addMovieDirector(MovieDirector movieDirector) {
        this.movieDirectorList.add(movieDirector);
    }

}
