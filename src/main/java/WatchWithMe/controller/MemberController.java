package WatchWithMe.controller;

import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/naver")
    public ApiResponse<String> loginByNaver(){ // 네이버 로그인 주소 전달
        String naverLoginUrl;

        naverLoginUrl = memberService.loginByNaver();
        return ApiResponse.onSuccess("로그인 주소를 조회했습니다.", naverLoginUrl);
    }

    @GetMapping("/login")
    public ApiResponse loginByToken(@RequestParam(name = "code") String code){
        Long memberId;

        memberId =  memberService.loginByToken(code);
        return ApiResponse.onSuccess("로그인에 성공하였습니다.", memberId);
    }

    @PostMapping("/logout/{memberId}")
    public ApiResponse logoutByToken(@PathVariable Long memberId){
        String message;

        message =  memberService.logoutByToken(memberId);
        return ApiResponse.onSuccess(message, "");
    }
}
