package WatchWithMe.controller;

import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/member")
public class MemberController {

    private final MemberService memberService;

}
