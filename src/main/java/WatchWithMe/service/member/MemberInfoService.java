package WatchWithMe.service.member;

import WatchWithMe.domain.Member;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        Member.Role role = Objects.requireNonNullElse(member.getRole(), Member.Role.USER);
        List<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role.name()));

        return MemberInfo.builder()
                .email(member.getEmail())
                .name(member.getName())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
