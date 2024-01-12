package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "name")
    private String name; // 배우 이름

    @OneToMany(mappedBy = "actor")
    private List<MovieActor> movieActorList = new ArrayList<>(); // 출연 영화 목록

}
