package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MovieActor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_actor_id")
    private Long movieActorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private Actor actor;

    // 생성 메서드
    public static MovieActor createMovieActor(Movie movie, Actor actor){
        MovieActor movieActor = new MovieActor();
        movieActor.movie = movie;
        movieActor.actor = actor;
        return movieActor;
    }

}
