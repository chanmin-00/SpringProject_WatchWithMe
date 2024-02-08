package WatchWithMe.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ActorListRequestDto(
        @NotBlank(message = "배우명을 입력해주세요")
        String name // 배우명
) {}
