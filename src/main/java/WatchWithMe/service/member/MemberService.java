package WatchWithMe.service.member;

import WatchWithMe.domain.Member;
import WatchWithMe.dto.request.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.global.config.jwt.TokenProvider;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
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

    // 회원 가입
    public Long save(SignUpRequestDto signUpRequestDto) {
        String password = passwordEncoder.encode(signUpRequestDto.password());
        Member member = Member.builder()
                .email(signUpRequestDto.email())
                .password(password)
                .name(signUpRequestDto.name())
                .mobile(signUpRequestDto.mobile())
                .role(Member.Role.USER)
                .build();
        save(member);
        return member.getMemberId();
    }

    public void save(Member member) {
        memberRepository.saveAndFlush(member);
    }

    // 로그인 인증 및 토큰 발급
    public LoginResponseDto authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 가지고 JWT AccessToken 발급
        String accessToken = tokenProvider.createToken(authentication);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    // 회원가입 아이디, 비밀번호 유효성 검사
    public boolean validateSignUpRequest(SignUpRequestDto signUpRequestDto){

        String email = signUpRequestDto.email();
        String password = signUpRequestDto.password();
        String confirmPassword = signUpRequestDto.confirmPassword();

        // 이메일 즁복 검사
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member != null)
            throw new GlobalException(GlobalErrorCode._DUPLICATE_EMAIL);

        // 비밀번호 일치 검사
        if (!password.equals(confirmPassword))
            throw new GlobalException(GlobalErrorCode._DIFF_PASSWORD);

        return true;

    }

}
