package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long directorId;

    @Column(name = "director_name")
    private String name; // 감독 이름

    @OneToMany(mappedBy = "director")
    private List<MovieDirector> movieDirectorList = new ArrayList<>(); // 출연 영화 목록

}
