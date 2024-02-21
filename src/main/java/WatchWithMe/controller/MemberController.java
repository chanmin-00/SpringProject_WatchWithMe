package WatchWithMe.controller;

import WatchWithMe.dto.request.ActorListRequestDto;
import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.dto.request.member.LoginRequestDto;
import WatchWithMe.dto.request.member.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.dto.response.MemberResponseDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.RecommendMovieService;
import WatchWithMe.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final RecommendMovieService recommendMovieService;

    // accessToken 발급
    @PostMapping("/token")
    @Operation(summary = "로그인", description = "이메일 및 비밀번호 입력 필요")
    public ApiResponse<LoginResponseDto> authorize(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        ApiResponse<LoginResponseDto> data;

        LoginResponseDto accessToken = memberService.authenticate(loginRequestDto.email(), loginRequestDto.password());
        data =  ApiResponse.onSuccess("로그인 성공, 토큰을 발급했습니다", accessToken);
        return data;
    }


    // 회원 가입 처리
    @PostMapping
    @Operation(summary = "회원 가입", description = "이메일, 비밀번호, 이름, 전화번호, 동의 확인 입력 필요")
    public ApiResponse<Long> join(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        Long memberId;
        ApiResponse<Long> data;
        memberService.validateSignUpRequest(signUpRequestDto);
        memberId = memberService.save(signUpRequestDto);
        data = ApiResponse.onSuccess("회원가입에 성공하였습니다.", memberId);
        return data;
    }

    // 회원 탈퇴
    @DeleteMapping("/delete/{memberId}")
    @Operation(summary = "회원 탈퇴", description = "memberId 입력 필요")
    public ApiResponse deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ApiResponse.onSuccess("회원 탈퇴에 성공하였습니다", "");
    }

    // 사용자 정보 조회
    @GetMapping("/get/{memberId}")
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보 반환")
    public ApiResponse<MemberResponseDto> getMember(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("사용자 정보 조회에 성공하였습니다", memberService.getMember(memberId));
    }

    // 사용자 기반 영화 추천 서비스
    @GetMapping("recommend/movie/{memberId}")
    @Operation(summary = "사용자 정보 기반 영화 추천", description = "영화 정보 리스트 반환")
    public ApiResponse recommendMovie(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("영화 추천에 성공하였습니다", recommendMovieService.recommendMovie(memberId));
    }

    // 선호 장르 업데이트
    @PutMapping("/update/favoriteGenre/{memberId}")
    @Operation(summary = "선호 장르 업데이트", description = "사용자 정보 기반 선호 장르 저장")
    public ApiResponse<Long> updateFavoriteGenre(@PathVariable Long memberId) {
        return ApiResponse.onSuccess("선호 장르 업데이트에 성공하였습니다", memberService.updateFavoriteGenre(memberId));
    }

    // 선호 배우 추가
    @PutMapping("/add/favoriteActor/{memberId}")
    @Operation(summary = "좋아하는 배우 추가", description = "배우명 입력 필요")
    public ApiResponse<Long> addFavoriteActor(@PathVariable Long memberId, @RequestBody ActorListRequestDto actorListRequestDto) {
        return ApiResponse.onSuccess("선호 배우 추가에 성공하였습니다", memberService.addFavoriteActor(memberId, actorListRequestDto));
    }

    // 선호 감독 추가
    @PutMapping("/add/favoriteDirector/{memberId}")
    @Operation(summary = "좋아하는 감독 추가", description = "감독명 입력 필요")
    public ApiResponse<Long> addFavoriteDirector(@PathVariable Long memberId, @RequestBody DirectorListRequestDto directorListRequestDto) {
        return ApiResponse.onSuccess("선호 감독 추가에 성공하였습니다", memberService.addFavoriteDirector(memberId, directorListRequestDto));
    }


}
