package WatchWithMe.service;

import WatchWithMe.domain.Member;
import WatchWithMe.global.exception.dto.GlobalException;
import WatchWithMe.repository.MemberRepository;
import WatchWithMe.service.naver.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final NaverLoginService naverLoginService;
    private final MemberRepository memberRepository;

    public String loginByNaver() throws GlobalException {
        String naverLonginUri = naverLoginService.requestNaverLogin();
        return naverLonginUri;
    }
    public Long loginByToken(String code) throws GlobalException {
        Long memberId;

        Member member =  naverLoginService.getNaverUserInfo(code);
        memberRepository.save(member);
        memberId = member.getMemberId();
        return memberId;
    }

    public String logoutByToken(Long memberId) throws GlobalException {
        Member member;
        String result;

        result =  naverLoginService.requestNaverLogout(memberId);
        member = memberRepository.findById(memberId).get(); // 토큰 값 널로 설정
        member.setAccessToken(null);
        memberRepository.save(member);
        return result;
    }
}
