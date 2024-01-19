package WatchWithMe.service.naver;

import WatchWithMe.domain.Member;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.service.naver.dto.NaverTokenResponseDto;
import WatchWithMe.service.naver.dto.NaverUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final NaverProperties naverProperties;

    public String requestNaverLogin(){ //로그인 인증 화면 주소 요청
        return naverProperties.getRequestAuthorizeUri();
    }

    public String requestNaverLogout(Long memberId){ // 토큰 삭제 후 로그아웃 요청

        String accessToken = memberRepository.findById(memberId).get().getAccessToken();

        ResponseEntity<String> response =
                restTemplate.exchange(naverProperties.getRequestDeleteTokenUrL(accessToken), HttpMethod.GET, null, String.class);
        if (response != null){
            return "로그아웃에 성공하셨습니다";
        }
        return "로그아웃에 실패하셨습니다";
    }

    public Member getNaverUserInfo(String code){ // 로그인 후에 토근 발급 후 회원 정보 얻어오기
        String accessToken = requestAccessToken(code);
        NaverUserResponseDto.NaverUserDetail profile = requestProfile(accessToken);

        Member member = new Member();
        member.setEmail(profile.getEmail());
        member.setName(profile.getName());
        member.setMobile(profile.getMobile());
        member.setAccessToken(accessToken);
        return member;
    }

    private String requestAccessToken(String code) {
        ResponseEntity<NaverTokenResponseDto> response =
                restTemplate.exchange(naverProperties.getRequestTokenURL(code), HttpMethod.GET, null, NaverTokenResponseDto.class);

        return response.getBody().getAccessToken();
    }

    private NaverUserResponseDto.NaverUserDetail requestProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<NaverUserResponseDto> response =
                restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, NaverUserResponseDto.class);

        return response.getBody().getNaverUserDetail();
    }

}
