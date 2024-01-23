package WatchWithMe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ActorListRequestDto {

    @NotBlank(message = "배우명을 입력해주세요")
    private String name; // 배우명
}
