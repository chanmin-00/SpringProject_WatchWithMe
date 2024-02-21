package WatchWithMe.dto.request.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank @Email
        String email,

        @NotBlank(message = "필수 입력값입니다")
        String password,

        @NotBlank(message = "필수 입력값입니다")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{5,}$",
                message = "영어, 숫자, 특수문자(@#$%^&+=!)를 모두 포함해야 합니다")
        @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다")
        String newPassword
)
{}
