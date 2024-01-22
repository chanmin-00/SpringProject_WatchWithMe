package WatchWithMe.repository.director;

import WatchWithMe.domain.Director;
import WatchWithMe.dto.request.DirectorListRequestDto;
import java.util.List;

public interface DirectorRepositoryCustom {
    List<Director> search(DirectorListRequestDto directorListRequestDto);
}
