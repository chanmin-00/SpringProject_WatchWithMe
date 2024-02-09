package WatchWithMe.dto.response.movie;

import WatchWithMe.domain.Movie;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
public class MoviePageResponseDto {

    private final int currentPage; // 현재 페이지
    private final int pageSize; // 페이지 안 아이템 개수
    private final int totalPage; // 총 페이지 개수
    private final int totalElement; //전체 아이템 개수
    private final List<MovieResponseDto> movieResponseDtoList;

    public MoviePageResponseDto(Page<Movie> moviePage) {

        this.movieResponseDtoList = moviePage.getContent().stream()
                .map(MovieResponseDto::new).toList();
        this.currentPage = moviePage.getNumber();
        this.pageSize = moviePage.getSize();
        this.totalPage = moviePage.getTotalPages();
        this.totalElement = moviePage.getNumberOfElements();

    }
}
