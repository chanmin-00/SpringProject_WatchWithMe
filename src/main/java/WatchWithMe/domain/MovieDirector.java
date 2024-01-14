package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MovieDirector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_director_id")
    private Long movieDirectorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Director director;

    // 생성 메서드
    public static MovieDirector createMovieDirector(Movie movie, Director director){
        MovieDirector movieDirector = new MovieDirector();
        movieDirector.movie = movie;
        movieDirector.director = director;
        return movieDirector;
    }

}
