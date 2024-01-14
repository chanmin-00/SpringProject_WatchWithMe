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

    @Column(name = "start_value") // 값이 null 인 경우 허용X
    private String tmp;

    @Column(name = "member_email") // 값이 null 인 경우 허용X
    private String email;

    @Column(name = "member_name", length = 10) // 값이 null 인 경우 허용X, 길이 제한 10
    private String name;

    private List<String> favoriteGenre = new ArrayList<>(); // 선호 장르 목록

    private List<String> favoriteActor = new ArrayList<>(); // 선호 배우 목록

    private List<String> favoriteDirector = new ArrayList<>(); // 선호 감독 목록

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>(); // 리뷰 리스트

}
