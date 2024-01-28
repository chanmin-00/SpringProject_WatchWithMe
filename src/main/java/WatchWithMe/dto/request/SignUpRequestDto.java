package WatchWithMe.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto (
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password,

    @NotBlank
    String confirmPassword,

    @NotBlank
    String name,

    @NotBlank
    String mobile,

    @AssertTrue
    boolean agree
){}
