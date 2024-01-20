package WatchWithMe.repository;

import WatchWithMe.domain.Actor;
import WatchWithMe.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member , Long> {
    Optional<Member> findByMobile(String mobile); // 전화번호 조회

    Optional<Member> findByEmail(String email); // 이메일 조회

}
