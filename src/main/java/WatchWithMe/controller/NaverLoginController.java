package WatchWithMe.controller;

import WatchWithMe.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NaverLoginController {

    private final MemberService memberService;

    @GetMapping("/loginForm")
    public String loginForm(){ // 네이버 로그인 주소 전달
        return "loginForm";
    }

}
