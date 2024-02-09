package WatchWithMe.dto.response;

import WatchWithMe.domain.Member;
import lombok.Getter;
import java.util.List;

@Getter
public class MemberResponseDto {

    private final String email; // 이메일
    private final String name; // 이름
    private final String mobile; // 휴대 전화 번호
    private final Member.Role role;
    private final List<String> favoriteGenre; // 선호 장르 목록
    private final List<String> favoriteActor; // 선호 배우 목록
    private final List<String> favoriteDirector; // 선호 감독 목록

    public MemberResponseDto(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.mobile = member.getMobile();
        this.role = member.getRole();
        this.favoriteGenre = member.getFavoriteGenre();
        this.favoriteActor = member.getFavoriteActor();
        this.favoriteDirector = member.getFavoriteDirector();
    }
}
