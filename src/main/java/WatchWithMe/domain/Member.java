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

    @Column(name = "member_email")
    private String email; // 이메일

    @Column(name = "member_name", length = 10) // 길이 제한 10
    private String name; // 이름

    private String mobile; // 휴대 전화번호

    private String accessToken; // 네이버 로그인 토큰 값, 토큰 값 존재할 경우만 접근 가능 설정

    private List<String> favoriteGenre = new ArrayList<>(); // 선호 장르 목록

    private List<String> favoriteActor = new ArrayList<>(); // 선호 배우 목록

    private List<String> favoriteDirector = new ArrayList<>(); // 선호 감독 목록

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>(); // 리뷰 리스트

}
