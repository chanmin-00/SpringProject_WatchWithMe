package WatchWithMe.repository.actor;

import WatchWithMe.domain.Actor;
import WatchWithMe.dto.request.ActorListRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static WatchWithMe.domain.QActor.actor;

@RequiredArgsConstructor
public class ActorRepositoryImpl implements ActorRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Actor> search(ActorListRequestDto actorListRequestDto, Pageable pageable){
        return queryFactory.select(actor).from(actor)
                .where(actorNameEq(actorListRequestDto.name()))
                .orderBy(actor.actorId.desc()) // 배우 id 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression actorNameEq(String name){
        if (name == null){
            return null;
        }
        return actor.name.eq(name);
    }
}
