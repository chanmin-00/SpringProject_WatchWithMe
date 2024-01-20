package WatchWithMe.service.naver;

import WatchWithMe.domain.Member;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.exception.dto.GlobalException;
import WatchWithMe.global.exception.dto.GlobalExceptionHandler;
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

        Member member;
        String accessToken; // 사용자 인증 토큰

        member = memberRepository.findById(memberId).orElse(null);
        if (member == null) { // DB에 존재하지 않는 경우
            throw new GlobalExceptionHandler(GlobalErrorCode._ACCOUNT_NOT_FOUND);
        }
        accessToken = member.getAccessToken();
        if (accessToken == null) { // 토큰이 존재하지 않는 경우
            throw new GlobalExceptionHandler(GlobalErrorCode._BAD_REQUEST);
        }

        ResponseEntity<String> response =
                restTemplate.exchange(naverProperties.getRequestDeleteTokenUrL(accessToken), HttpMethod.GET, null, String.class);
        if (response == null)
            throw new GlobalExceptionHandler(GlobalErrorCode._INTERNAL_SERVER_ERROR);

        return "로그아웃에 성공하셨습니다";
    }

    public Member getNaverUserInfo(String code){ // 로그인 후에 토근 발급 후 회원 정보 얻어오기

        String accessToken;
        NaverUserResponseDto.NaverUserDetail profile;

        try {
            accessToken = requestAccessToken(code);
            profile = requestProfile(accessToken);
        }
        catch (GlobalException e){
            throw new GlobalExceptionHandler(GlobalErrorCode._INTERNAL_SERVER_ERROR);
        }

        Member member = new Member();
        member.setEmail(profile.getEmail());
        member.setName(profile.getName());
        member.setMobile(profile.getMobile());
        member.setAccessToken(accessToken);
        return member;
    }

    private String requestAccessToken(String code) throws GlobalException {
        ResponseEntity<NaverTokenResponseDto> response =
                restTemplate.exchange(naverProperties.getRequestTokenURL(code), HttpMethod.GET, null, NaverTokenResponseDto.class);

        return response.getBody().getAccessToken();
    }

    private NaverUserResponseDto.NaverUserDetail requestProfile(String accessToken) throws GlobalException{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<NaverUserResponseDto> response =
                restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, NaverUserResponseDto.class);

        return response.getBody().getNaverUserDetail();
    }

}
