package WatchWithMe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DirectorListRequestDto {
    @NotBlank(message = "감독명을 입력해주세요")
    private String name; // 감독명
}
