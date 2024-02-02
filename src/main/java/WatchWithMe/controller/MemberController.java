package WatchWithMe.controller;

import WatchWithMe.dto.request.member.LoginRequestDto;
import WatchWithMe.dto.request.member.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
     accessToken 발급
     */
    @PostMapping("/token")
    public ApiResponse<LoginResponseDto> authorize(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        ApiResponse<LoginResponseDto> data;

        LoginResponseDto accessToken = memberService.authenticate(loginRequestDto.email(), loginRequestDto.password());
        data =  ApiResponse.onSuccess("로그인 성공, 토큰을 발급했습니다", accessToken);
        return data;
    }


    /*
     회원 가입 처리
     */
    @PostMapping
    public ApiResponse<Long> join(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        Long memberId;
        ApiResponse<Long> data;
        memberService.validateSignUpRequest(signUpRequestDto);
        memberId = memberService.save(signUpRequestDto);
        data = ApiResponse.onSuccess("회원가입에 성공하였습니다.", memberId);
        return data;
    }
}
