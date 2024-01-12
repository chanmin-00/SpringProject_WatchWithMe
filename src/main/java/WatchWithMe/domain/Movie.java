package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "title")
    private String title; // 영화 제목

    @Column(name = "year")
    private Long year; // 제작 연도

    @Column(name = "user_rating")
    private Double userRating; // 평균 평점

    @Column(name = "genre")
    private String genre; // 장르

    @OneToMany(mappedBy = "movie")
    @Column(name = "review_list")
    private List<Review> reviewList = new ArrayList<>(); // 리뷰 목록

    @OneToMany(mappedBy = "movie")
    private List<MovieActor> movieActorList = new ArrayList<>(); // 출연 배우 목록

    @OneToMany(mappedBy = "movie")
    private List<MovieDirector> movieDirectorList = new ArrayList<>(); // 출연 감독 목록

}
