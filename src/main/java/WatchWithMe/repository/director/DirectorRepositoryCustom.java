package WatchWithMe.repository.director;

import WatchWithMe.domain.Director;
import WatchWithMe.dto.request.DirectorListRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DirectorRepositoryCustom {
    List<Director> search(DirectorListRequestDto directorListRequestDto, Pageable pageable);
}
