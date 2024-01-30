package WatchWithMe.global.config.jwt;

import WatchWithMe.global.exception.code.GlobalErrorCode;
import WatchWithMe.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ApiResponse<String> data;
        ObjectMapper objectMapper = new ObjectMapper();

        // 인증 없이 페이지 접근시 접근 권한 없음(401)
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        data = ApiResponse.onFailure(GlobalErrorCode._UNAUTHORIZED_TOKEN, "");

        // Json 형식의 문자열로 변경
        String result = objectMapper.writeValueAsString(data);
        response.getWriter().write(result);
    }

}


