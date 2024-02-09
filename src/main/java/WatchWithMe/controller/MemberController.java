package WatchWithMe.controller;

import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.dto.request.member.LoginRequestDto;
import WatchWithMe.dto.request.member.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.dto.response.MemberResponseDto;
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

    /*
    사용자 정보 조회
     */
    @GetMapping("/get/{memberId}")
    public ApiResponse<MemberResponseDto> getMember(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("사용자 정보 조회에 성공하였습니다", memberService.getMember(memberId));
    }

    /*
    선호 장르 업데이트
    */
    @PutMapping("/update/favoriteGenre/{memberId}")
    public ApiResponse<Long> updateFavoriteGenre(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("선호 장르 업데이트에 성공하였습니다", memberService.updateFavoriteGenre(memberId));
    }

    /*
    선호 배우 추가
     */
    @PutMapping("/add/favoriteActor/{memberId}")
    public ApiResponse<Long> addFavoriteActor(@PathVariable Long memberId, @RequestBody ActorListRequestDto actorListRequestDto) {
        return ApiResponse.onSuccess("선호 배우 추가에 성공하였습니다", memberService.addFavoriteActor(memberId, actorListRequestDto));
    }

    /*
    선호 감독 추가
     */
    @PutMapping("/add/favoriteDirector/{memberId}")
    public ApiResponse<Long> addFavoriteDirector(@PathVariable Long memberId, @RequestBody DirectorListRequestDto directorListRequestDto) {
        return ApiResponse.onSuccess("선호 감독 추가에 성공하였습니다", memberService.addFavoriteDirector(memberId, directorListRequestDto));
    }


}
