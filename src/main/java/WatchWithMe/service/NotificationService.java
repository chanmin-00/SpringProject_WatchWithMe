package WatchWithMe.service;

import WatchWithMe.domain.Member;
import WatchWithMe.global.exception.GlobalException;
import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.repository.EmitterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SseEmitter subscribe(String email, String lastEventId) {

        String emitterId = email + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // 상황별 emitter 삭제 처리
        sseEmitter.onCompletion(() -> emitterRepository.deleteEmitterById(emitterId)); //완료 시
        sseEmitter.onTimeout(() -> emitterRepository.deleteEmitterById(emitterId)); // 타임 아웃 시
        sseEmitter.onError((e) -> emitterRepository.deleteEmitterById(emitterId)); // 에러 발생 시

        // 503 Service Unavailable 방지용 dummy event 전송
        sendEventToClient(sseEmitter, emitterId, "EventStream Created. [email = " + email + " ]");

        // client가 미수신한 event 목록이 존재하는 경우
        if (!lastEventId.isEmpty()) {
            Map<String, Object> eventCaches = emitterRepository.findAllEventCacheByEmail(email);
            eventCaches.entrySet().stream() //미수신 상태인 event 목록 전송
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0) // lastEventId 이후의 event만 전송
                    .forEach(entry -> sendEventToClient(sseEmitter, entry.getKey(), entry.getValue()));
        }

        return sseEmitter;
    }

    public void send(Member receiver, Object data) {
        String emitterId = null;
        try {
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByEmail(receiver.getEmail());

            if (emitters.isEmpty()) {
                return;
            }

            emitterId = emitters.keySet().iterator().next();

            emitters.forEach((key, value) -> {
                emitterRepository.saveEventCache(key, data);
                sendEventToClient(value, key, data);
            });
        } catch (Exception e) {
            emitterRepository.deleteEmitterById(emitterId);
            throw new GlobalException(GlobalErrorCode._INTERNAL_SERVER_ERROR);
        }
    }

    private void sendEventToClient(SseEmitter sseEmitter, String emitterId, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            sseEmitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(jsonData, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            emitterRepository.deleteEmitterById(emitterId);
            sseEmitter.completeWithError(exception);
            throw new GlobalException(GlobalErrorCode._INTERNAL_SERVER_ERROR);
        }
    }
}
