package WatchWithMe.repository.director;

import WatchWithMe.domain.Director;
import WatchWithMe.dto.request.DirectorListRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static WatchWithMe.domain.QDirector.director;

@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Director> search(DirectorListRequestDto directorListRequestDto, Pageable pageable){
        return queryFactory.select(director).from(director)
                .where(directorNameEq(directorListRequestDto.name()))
                .orderBy(director.directorId.desc()) // 감독 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression directorNameEq(String name){
        if (name == null){
            return null;
        }
        return director.name.eq(name);
    }
}
