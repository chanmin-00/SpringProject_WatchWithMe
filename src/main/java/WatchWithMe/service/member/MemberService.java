package WatchWithMe.service.member;

import WatchWithMe.domain.Member;
import WatchWithMe.dto.request.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.global.config.jwt.TokenProvider;
import WatchWithMe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository repository;

    public void save(SignUpRequestDto signUpRequestDto) {
        String password = passwordEncoder.encode(signUpRequestDto.password());
        Member member = Member.builder()
                .email(signUpRequestDto.email())
                .password(password)
                .name(signUpRequestDto.name())
                .mobile(signUpRequestDto.mobile())
                .role(Member.Role.USER)
                .build();
        save(member);
    }

    public void save(Member member) {

        memberRepository.saveAndFlush(member);
    }

    public LoginResponseDto authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 가지고 JWT AccessToken 발급
        String accessToken = tokenProvider.createToken(authentication);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }


}
