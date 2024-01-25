package WatchWithMe.service.oauth2;

import WatchWithMe.domain.Member;
import WatchWithMe.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        if(provider.equals("google")) {
            log.info("구글 로그인 요청");
            //oAuth2UserInfo = new GoogleUserInfo( oAuth2User.getAttributes() );
        } else if(provider.equals("kakao")) {
            log.info("카카오 로그인 요청");
            //oAuth2UserInfo = new KakaoUserInfo( (Map)oAuth2User.getAttributes() );
        } else if(provider.equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo( (Map) oAuth2User.getAttribute("response"));
        } else if(provider.equals("facebook")) {
            log.info("페이스북 로그인 요청");
            //oAuth2UserInfo = new FacebookUserInfo( oAuth2User.getAttributes() );
        }
        String email = oAuth2UserInfo.getEmail();
        String nickname = oAuth2UserInfo.getName();
        String mobile = oAuth2UserInfo.getMobile();
        // String providerId = oAuth2UserInfo.getProviderId();
        // String loginId = provider + "_" + providerId;
        Member member = memberRepository.findByEmail(email).orElse(null);
        Member newMember = null;
        if(member == null) {
            newMember = new Member();
            newMember.setEmail(email);
            newMember.setName(nickname);
            newMember.setMobile(mobile);
            newMember.setRole(Member.Role.MEMBER);
            memberRepository.save(newMember);
        } else {
            newMember = member;
        }
        return new PrincipalDetails(newMember, oAuth2User.getAttributes());
    }
}
