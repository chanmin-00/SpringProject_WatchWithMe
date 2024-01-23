package WatchWithMe.repository.director;

import WatchWithMe.domain.Director;
import WatchWithMe.dto.request.DirectorListRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static WatchWithMe.domain.QDirector.director;

@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Director> search(DirectorListRequestDto directorListRequestDto){
        return queryFactory.select(director).from(director)
                .where(directorNameEq(directorListRequestDto.getName())).fetch();
    }

    private BooleanExpression directorNameEq(String name){
        if (name == null){
            return null;
        }
        return director.name.eq(name);
    }
}
