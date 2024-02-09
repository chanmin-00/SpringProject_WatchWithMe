package WatchWithMe.service.member;

import WatchWithMe.domain.Actor;
import WatchWithMe.domain.Director;
import WatchWithMe.domain.Member;
import WatchWithMe.domain.Review;
import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.dto.request.member.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.global.config.jwt.TokenProvider;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.repository.actor.ActorRepository;
import WatchWithMe.repository.director.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
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

    // 선호 장르 목록 업데이트
    public Long updateFavoriteGenre(Long memberId){

        Map<String, Long> genreCountMap = new HashMap<>(); // 사용자 작성 리뷰의 영화 장르 개수 카운트

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        // 장르별 개수 세기
        List<Review> reviewList = member.getReviewList();
        for(Review review : reviewList) {
            String genre = review.getMovie().getGenre();
            genreCountMap.compute(genre, (key, value) -> (value == null) ? 1L : value + 1L);
        }

        // 장르별 개수 중 상위 5위 장르 추출
        List<String> favoriteGenre = genreCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        member.setFavoriteGenre(favoriteGenre);
        memberRepository.save(member);
        return member.getMemberId();
    }

    // 선호 배우 추가
    public Long addFavoriteActor(Long memberId, ActorListRequestDto actorListRequestDto) {

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        Actor actor = actorRepository.findByName(actorListRequestDto.name()).orElse(null);
        if (actor == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        member.addFavoriteActor(actor.getName());
        memberRepository.save(member);
        return memberId;
    }

    // 선호 감독 추가
    public Long addFavoriteDirector(Long memberId, DirectorListRequestDto directorListRequestDto) {

        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null)
            throw new GlobalException(GlobalErrorCode._ACCOUNT_NOT_FOUND);

        Director director = directorRepository.findByName(directorListRequestDto.name()).orElse(null);
        if (director == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);

        member.addFavoriteDirector(director.getName());
        memberRepository.save(member);
        return memberId;
    }
}
