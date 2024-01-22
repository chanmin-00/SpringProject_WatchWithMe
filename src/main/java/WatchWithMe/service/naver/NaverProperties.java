package WatchWithMe.service.naver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@Configuration
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {

    private String requestAuthorizeUrl; // 로그인 요청 화면 주소
    private String requestTokenUrl; // 토근 접근 화면 주소
    private String redirectUrl;
    private String clientId;
    private String clientSecret;

    public String getRequestAuthorizeUrl() throws Exception { // 로그인 인증 요청 화면 주소 가져오기
        return UriComponentsBuilder.fromHttpUrl(requestAuthorizeUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUrl)
                .toUriString();
    }

    public String getRequestTokenUrl(String code) throws Exception { // 토큰 요청 화면 주소 가져오기
        return UriComponentsBuilder.fromHttpUrl(requestTokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .toUriString();
    }

    public String getRequestDeleteTokenUrl(String accessToken) throws Exception{ // 로그아웃 화면 불러오기
        return UriComponentsBuilder.fromHttpUrl(requestTokenUrl)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .toUriString();
    }
}
