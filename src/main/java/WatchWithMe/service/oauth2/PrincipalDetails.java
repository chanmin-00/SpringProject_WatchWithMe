package WatchWithMe.service.oauth2;

import WatchWithMe.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member, Map<String, Object> attributes){
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    // 권한 관련 작업을 하기 위한 role return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add(() -> {
            return member.getName();
        });

        return collections;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


}
