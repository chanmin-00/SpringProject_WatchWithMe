package WatchWithMe.service.naver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Configuration
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {

    private String requestAuthorizeUri; // 로그인 요청 화면 주소
    private String requestTokenUri; // 토근 접근 화면 주소
    private String redirectUri;
    private String clientId;
    private String clientSecret;

    public String getRequestAuthorizeUri(){ // 로그인 인증 요청 화면 주소 가져오기
        return UriComponentsBuilder.fromHttpUrl(requestAuthorizeUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    public String getRequestTokenURL(String code){ // 토큰 요청 화면 주소 가져오기
        return UriComponentsBuilder.fromHttpUrl(requestTokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .toUriString();
    }

    public String getRequestDeleteTokenUrL(String accessToken){ // 로그아웃 화면 불러오기
        return UriComponentsBuilder.fromHttpUrl(requestTokenUri)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .toUriString();
    }
}
