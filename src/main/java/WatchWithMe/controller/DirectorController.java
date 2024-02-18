package WatchWithMe.controller;

import WatchWithMe.dto.request.DirectorListRequestDto;
import WatchWithMe.global.response.ApiResponse;
import WatchWithMe.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/director")
public class DirectorController {

    private final DirectorService directorService;

    // 영화 감독별 영화 검색
    @PostMapping("/search")
    @Operation(summary = "감독 영화 검색", description = "감독명 입력 필요")
    public ApiResponse search(@Valid @RequestBody DirectorListRequestDto directorListRequestDto, @Valid @RequestParam(value="page", defaultValue="1") int page) {
        return ApiResponse.onSuccess("영화 조건 검색에 성공했습니다", directorService.searchMovieListByDirector(directorListRequestDto, page));
    }
}
