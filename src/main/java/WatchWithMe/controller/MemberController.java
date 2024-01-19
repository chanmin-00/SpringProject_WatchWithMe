package WatchWithMe.controller;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.exception.dto.GlobalException;
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
    public ApiResponse<String> loginByNaver(){ // 네이버 로그인 화면 주소 요청
        String naverLoginUri = memberService.loginByNaver();
        return ApiResponse.onSuccess("로그인 주소를 조회했습니다.", naverLoginUri);
    }

    @GetMapping("/login")
    public ApiResponse loginByToken(@RequestParam(name = "code") String code){
        Long memberId;
        try {
            memberId =  memberService.loginByToken(code);
        }
        catch(GlobalException e){
            return ApiResponse.onFailure(GlobalErrorCode._INTERNAL_SERVER_ERROR, "");
        }
        return ApiResponse.onSuccess("로그인에 성공하였습니다.", memberId);
    }

    @PostMapping("/logout/{memberId}")
    public ApiResponse logoutByToken(@PathVariable Long memberId){
        String message;
        try {
            message =  memberService.logoutByToken(memberId);
        }
        catch(GlobalException e){
            return ApiResponse.onFailure(GlobalErrorCode._INTERNAL_SERVER_ERROR, "");
        }
        return ApiResponse.onSuccess(message, "");
    }
}
