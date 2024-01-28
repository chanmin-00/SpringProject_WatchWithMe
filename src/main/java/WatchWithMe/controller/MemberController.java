package WatchWithMe.controller;

import WatchWithMe.dto.request.LoginRequestDto;
import WatchWithMe.dto.request.SignUpRequestDto;
import WatchWithMe.dto.response.LoginResponseDto;
import WatchWithMe.global.config.jwt.CustomJwtFilter;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.member.MemberInfoService;
import WatchWithMe.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberInfoService infoService;

    /**
     * accessToken 발급
     *
     */
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<LoginResponseDto>> authorize(@RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto token = memberService.authenticate(loginRequestDto.email(), loginRequestDto.password());

        HttpHeaders headers = new HttpHeaders();
        headers.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.accessToken());

        ApiResponse<LoginResponseDto> data = ApiResponse.onSuccess("accessToken", token);

        return ResponseEntity.status(200)
                .headers(headers)
                .body(data);
    }


    /**
     * 회원가입 처리
     *
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> join(@RequestBody SignUpRequestDto signUpRequestDto) {

        memberService.save(signUpRequestDto);

        HttpStatus status = HttpStatus.CREATED.CREATED;
        ApiResponse data = ApiResponse.onSuccess("signUp", null);

        return ResponseEntity.status(status).body(data);
    }


    @GetMapping("/member_only")
    public void MemberOnlyUrl() {
        log.info("회원 전용 URL 접근 테스트");
    }

    @GetMapping("/admin_only")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void adminOnlyUrl() {
        log.info("관리자 전용 URL 접근 테스트");
    }
}
