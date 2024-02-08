package WatchWithMe.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DirectorListRequestDto(
        @NotBlank(message = "감독명을 입력해주세요")
        String name // 감독명
) {}
