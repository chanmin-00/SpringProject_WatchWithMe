package WatchWithMe.service;

import WatchWithMe.domain.Member;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.service.naver.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final NaverLoginService naverLoginService;
    private final MemberRepository memberRepository;

    public String loginByNaver() {
        String naverLonginUrl = naverLoginService.requestNaverLogin();
        return naverLonginUrl;
    }

    public Long loginByToken(String code) {
        Long memberId;

        Member member =  naverLoginService.getNaverUserInfo(code);
        Member saveMember = memberRepository.findByMobile(member.getMobile()).orElse(null);
        if (saveMember != null){ // 이미 저장된 멤버인 경우
            saveMember.setAccessToken(member.getAccessToken()); // 토큰 값 할당
            return saveMember.getMemberId();
        }

        memberRepository.save(member); // 새로 DB 저장
        memberId = member.getMemberId();
        return memberId;
    }

    public String logoutByToken(Long memberId) {
        Member member;
        String result;

        result =  naverLoginService.requestNaverLogout(memberId);
        member = memberRepository.findById(memberId).orElse(null);
        if (member != null){
            member.setAccessToken(null); // 토큰 값 널로 설정
        }
        return result;
    }
}
