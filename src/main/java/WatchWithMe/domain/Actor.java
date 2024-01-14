package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_name")
    private String name; // 배우 이름

    @OneToMany(mappedBy = "actor")
    private List<MovieActor> movieActorList = new ArrayList<>(); // 출연 영화 목록

    // 생성 메서드
    public static Actor createActor(String actorName){
        Actor actor = new Actor();
        actor.name = actorName;
        return actor;
    }

    //연관 관계 메서드//
    public void addMovieActor(MovieActor movieActor) {
        this.movieActorList.add(movieActor);
    }

}
