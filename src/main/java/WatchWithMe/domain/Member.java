package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email", nullable = false) // 값이 null 인 경우 허용X
    private String email;

    @Column(name = "member_name", nullable = false, length = 10) // 값이 null 인 경우 허용X, 길이 제한 10
    private String name;

    @Column(name = "favorite_genre_list")
    private List<String> favoriteGenre = new ArrayList<>(); // 선호 장르 목록

    @Column(name = "favorite_actor_list")
    private List<String> favoriteActor = new ArrayList<>(); // 선호 배우 목록

    @Column(name = "favorite_director_list")
    private List<String> favoriteDirector = new ArrayList<>(); // 선호 감독 목록

    @OneToMany(mappedBy = "member")
    @Column(name = "review_list")
    private List<Review> reviewList = new ArrayList<>(); // 리뷰 리스트

}
