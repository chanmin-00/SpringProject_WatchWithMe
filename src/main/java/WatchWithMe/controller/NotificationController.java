package WatchWithMe.controller;


import WatchWithMe.domain.Member;
import WatchWithMe.service.NotificationService;
import WatchWithMe.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;

    @GetMapping(path = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 구독", description = "알림 구독을 위한 SseEmitter 생성")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long memberId, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(notificationService.subscribe(member.getEmail(), lastEventId));
    }

}