package WatchWithMe.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String accessToken
) {}
