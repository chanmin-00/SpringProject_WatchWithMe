package WatchWithMe.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email")
    private String email; // 이메일

    @Column(name = "member_name", length = 10) // 길이 제한 10
    private String name; // 이름

    @Column(length=65, nullable = false)
    private String password;

    private String mobile; // 휴대 전화번호

    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable = false)
    private Role role;

    private List<String> favoriteGenre; // 선호 장르 목록

    private List<String> favoriteActor; // 선호 배우 목록

    private List<String> favoriteDirector; // 선호 감독 목록

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList; // 리뷰 리스트

    public enum Role {
        USER, ADMIN
    }
}
