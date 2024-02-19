package WatchWithMe.dto.request.review;

import jakarta.validation.constraints.Size;

public record SearchReviewRequestDto(
        @Size(max = 30, min = 2, message = "검색어 제한 길이는 30자 이내, 2글자 이상입니다")
        String searchText // 검색 텍스트
)
{}
