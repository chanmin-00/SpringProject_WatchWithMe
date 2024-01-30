package WatchWithMe.global.config.jwt;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        ApiResponse<String> data;
        ObjectMapper objectMapper = new ObjectMapper();

        // 인증은 했으나 페이지 접근시 권한 없음(403)
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        data = ApiResponse.onFailure(GlobalErrorCode._FORBIDDEN, "");

        // Json 형식의 문자열로 변경
        String result = objectMapper.writeValueAsString(data);
        response.getWriter().write(result);

    }
}
